package com.indracompany.walle.infrastructure.service;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.indracompany.walle.application.service.GenericCrudServiceImpl;
import com.indracompany.walle.domain.model.DeParaMensagens;
import com.indracompany.walle.domain.model.HistoricoMensagens;
import com.indracompany.walle.domain.model.Mensagem;
import com.indracompany.walle.domain.model.UsuarioWhats;
import com.indracompany.walle.infrastructure.persistence.hibernate.repository.DeParaMensagensRepository;
import com.indracompany.walle.infrastructure.persistence.hibernate.repository.HistoricoMensagensRepository;
import com.indracompany.walle.infrastructure.persistence.hibernate.repository.MensagensRepository;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MensagensService extends GenericCrudServiceImpl<Mensagem, Long, MensagensRepository> {

	private static final long PRIMEIRA_MENSAGEM_ID = 1L;
	private static final long DOAR_NOVAMENTE_MENSAGEM_ID = 6L;
	private static final long AGRADECIMENTO_MENSAGEM_ID = 7L;
	
	@Autowired
	private ParametroSistemaService parametroSistemaService;
	
	@Autowired
	private HistoricoMensagensRepository historicoMensagensRepository;
	
	@Autowired
	private DeParaMensagensRepository deParaMensagensRepository;
	
	public void enviaResposta(UsuarioWhats usuario, String mensagem) {
		HistoricoMensagens ultimaMensagem = historicoMensagensRepository.findFirstByUsuarioIdOrderByIdDesc(usuario.getId());
		Mensagem mensagemResposta = processaMensagem(mensagem, ultimaMensagem);
		
		if (Objects.nonNull(mensagemResposta)) {
			String numero = ConverterService.convertRemoteJidToNumber(usuario.getRemoteJid());
			
			// Envia mensagem com com Boleto e Agradecimento.
			if (StringUtils.isNotBlank(mensagemResposta.getArquivo())) {
				this.enviarMensagemComArquivos(mensagemResposta.getDescMensagem(), mensagemResposta.getArquivo(), numero);
				salvaMensagemNoHistorico(usuario, mensagemResposta);
				
				Mensagem mensagemAgradecimento = buscar(AGRADECIMENTO_MENSAGEM_ID);
				this.enviarMensagemComArquivos(
						processaMensagemAgradecimento(mensagemAgradecimento, mensagem)
						, mensagemAgradecimento.getArquivo()
						, numero);
				salvaMensagemNoHistorico(usuario, mensagemAgradecimento);

			// Envia próxima mensagem.
			} else  {
				this.enviarMensagem(mensagemResposta.getDescMensagem() , numero);
				salvaMensagemNoHistorico(usuario, mensagemResposta);
			}
			
		}
	}
	
	public String processaMensagemAgradecimento(Mensagem mensagemAgradecimento, String valor) {
		return mensagemAgradecimento.getDescMensagem().replaceAll("<valor/>", valor);
	}

	public Mensagem processaMensagem(String mensagem, HistoricoMensagens ultimaMensagem) {
		Mensagem mensagemRetorno = null;
		
		if (Objects.nonNull(ultimaMensagem)) {
			DeParaMensagens deParaMensagem = deParaMensagensRepository.
					findByMensagemDeIdAndResposta(ultimaMensagem.getMensagem().getId(), mensagem);
			
			if (Objects.nonNull(deParaMensagem)) {
				mensagemRetorno = deParaMensagem.getMensagemPara();
			
			// Caso o usuario já tenha feito uma doação uma outra mensagem para efetuar outra doação
			// será enviada para o mesmo. (somente se a ultima já n tenha sido essa, para evitar spam.)
			} else if (ultimaMensagem.getMensagem().getId() != PRIMEIRA_MENSAGEM_ID
					&& ultimaMensagem.getMensagem().getId() != DOAR_NOVAMENTE_MENSAGEM_ID) { 
				mensagemRetorno = buscar(DOAR_NOVAMENTE_MENSAGEM_ID);
			}
		} else {
			mensagemRetorno = buscar(PRIMEIRA_MENSAGEM_ID);
		}
		return mensagemRetorno;
	}
	
	private void salvaMensagemNoHistorico(UsuarioWhats usuarioWhats, Mensagem mensagem) {
		HistoricoMensagens historicoMensagens = new HistoricoMensagens();
		historicoMensagens.setUsuario(usuarioWhats);
		historicoMensagens.setMensagem(mensagem);
		historicoMensagensRepository.save(historicoMensagens);
	}

	
	public void enviarMensagem(String mensagem, String numero) {
		try {
			String url = parametroSistemaService.obterParametroSistema("URL").getValorParametro();
			String sendMessageEP = url + parametroSistemaService.obterParametroSistema("SEND_MESSAGE_EP").getValorParametro();
			String token = parametroSistemaService.obterParametroSistema("TOKEN").getValorParametro();
			HttpResponse response = Unirest.post(sendMessageEP)
					.header("Authorization", token)
					.header("cache-control", "no-cache")
					.body("{\"menssage\":\""+mensagem+"\",\"number\":\""+numero+"\"}")
					.asString();
			
			log.info("Response Status: " + response.getStatus());
		} catch (UnirestException e) {
			log.error(e.getMessage());
		}
	}
	
	public void enviarMensagemComArquivos(String nomeArquivo, String urlArquivoRetorno, String numero) {
		try {
			String url = parametroSistemaService.obterParametroSistema("URL").getValorParametro();
			String sendMessageFileFromUrlEP = url + parametroSistemaService.obterParametroSistema("SEND_MESSAGE_FILE_FROM_URL_EP").getValorParametro();
			String token = parametroSistemaService.obterParametroSistema("TOKEN").getValorParametro();
			HttpResponse response = Unirest.post(sendMessageFileFromUrlEP)
					.header("Authorization", token)
					.header("cache-control", "no-cache")
					.body("{\"caption\":\""+nomeArquivo+"\",\"number\":\""+numero+"\",\"url\":\""+urlArquivoRetorno+"\"}")
					.asString();
			
			log.info("Response Status: " + response.getStatus());
		} catch (UnirestException e) {
			log.error(e.getMessage());
		}

	}
}
