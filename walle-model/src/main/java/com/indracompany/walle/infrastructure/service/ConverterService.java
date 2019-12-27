package com.indracompany.walle.infrastructure.service;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import com.indracompany.walle.application.service.exception.AplicacaoException;
import com.indracompany.walle.application.service.exception.WalleValidacoes;
import com.indracompany.walle.infrastructure.annotation.converter.IdReference;
import com.indracompany.walle.infrastructure.factory.RepositoryFactory;
import com.indracompany.walle.infrastructure.util.FieldUtils;

@Service
public class ConverterService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private RepositoryFactory repositoryFactory;

	public static String convertRemoteJidToNumber(String remoteJid) {
		return remoteJid.substring(0, remoteJid.indexOf(remoteJid.contains("-") ? "-" : "@"));
	}

	public <T> T convert(Object data, Class<T> destinationType) {
		T target = modelMapper.map(data, destinationType);

		return refreshReferences(data, target);
	}

	public <T> Page<T> convert(Page<?> dataPage, Class<T> destinationType) {
		return dataPage.map(data -> convert(data, destinationType));
	}

	public <T> List<T> convert(List<?> dataList, Class<T> destinationType) {
		return dataList.stream().map(data -> convert(data, destinationType)).collect(Collectors.toList());
	}

	private <T> T refreshReferences(Object data, T target) {
		Field[] fields = data.getClass().getDeclaredFields();

		for (Field field : fields) {
			IdReference idReference = field.getAnnotation(IdReference.class);

			if (idReference != null) {
				field.setAccessible(true);

				try {
					SimpleJpaRepository<?, Long> repository = repositoryFactory.create(idReference.target());
					Field fieldTarget = target.getClass().getDeclaredField(idReference.property());
					fieldTarget.setAccessible(true);

					Object fieldValue = field.get(data);

					if (fieldValue == null) {
						continue;
					}

					if (field.get(data) instanceof List) {
						fieldTarget.set(target, repository.findAllById(FieldUtils.getLongValues(data, field)));
					} else if (field.get(data) instanceof Set) {
						fieldTarget.set(target,
								new HashSet<>(repository.findAllById(FieldUtils.getLongValues(data, field))));
					} else {
						Optional<?> value = repository.findById((Long) fieldValue);

						if (!value.isPresent()) {
							throw new AplicacaoException(WalleValidacoes.ERRO_ACESSO_SISTEMA, fieldTarget.getName());
						}

						fieldTarget.set(target, value.get());
					}
				} catch (NoSuchFieldException | IllegalAccessException exception) {
					throw new AplicacaoException(WalleValidacoes.ERRO_ACESSO_SISTEMA, exception);
				}
			}
		}

		return target;
	}

}
