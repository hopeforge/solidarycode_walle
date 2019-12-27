package com.indracompany.walle.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indracompany.walle.domain.model.UsuarioWhats;
import com.indracompany.walle.infrastructure.service.MensagensService;
import com.indracompany.walle.infrastructure.service.UsuarioWhatsService;
import com.indracompany.walle.presentation.dto.MessageReceived;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/message", produces = { "application/json" })
@Api(value = "message")
public class MessageRestController {

	@Autowired
	private UsuarioWhatsService usuarioWhatsService;

	@Autowired
	private MensagensService mensagensService;


	@PostMapping(value = "/receberMenssagem")
	public @ResponseBody ResponseEntity<Void> receberMenssagem(@ApiParam(value = "Recebimento de Mensagem da API", required = true) final @RequestBody MessageReceived value) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (value != null) {
				String jsonInString = mapper.writeValueAsString(value);
				UsuarioWhats usuarioWhats = usuarioWhatsService.createUsuario(value.getBody().getInfo().getRemoteJid());
				mensagensService.enviaResposta(usuarioWhats,
						value.getBody().getInfo().getSource().getMessage().getConversation());
				log.info(jsonInString);
			}
		} catch (Exception e) {
			log.error("erro ao receberMenssagem() ", e);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
