package com.indracompany.walle.infrastructure.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.indracompany.walle.application.service.exception.AplicacaoException;
import com.indracompany.walle.application.service.exception.WalleValidacoes;

public class JSONUtil {

	public static String convertObjectToJsonStringWithIgnore(Object object, String ... propertiesIgnore) throws AplicacaoException {

		try {

			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(
					JsonAutoDetect.Visibility.ANY));
		    FilterProvider filters = new SimpleFilterProvider()
		      .addFilter("filter properties by name",
		          SimpleBeanPropertyFilter.serializeAllExcept(
		        		  propertiesIgnore));

		    ObjectWriter writer = mapper.writer(filters);

		    return writer.writeValueAsString(object);
		} catch (Exception e) {
			throw new AplicacaoException(WalleValidacoes.ERRO_SERIALIZAR_JSON, e);
		}
    }

	public static String convertObjectToJsonString(Object object) throws AplicacaoException {

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(
					JsonAutoDetect.Visibility.ANY));
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			throw new AplicacaoException(WalleValidacoes.ERRO_SERIALIZAR_JSON, e);
		}
    }

    public static <T> T convertJsonToObject(byte[] src, Class<T> valueType) throws AplicacaoException {

    	try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(
					JsonAutoDetect.Visibility.ANY));
			return mapper.readValue(src, valueType);
		} catch (Exception e) {
			throw new AplicacaoException(WalleValidacoes.ERRO_SERIALIZAR_JSON, e);
		}
    }

	public static <T> T convertJsonStringToObject(String json, Class<T> object) throws AplicacaoException {

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(
					JsonAutoDetect.Visibility.ANY));
			return mapper.readValue(json, object);
		} catch (Exception e) {
			throw new AplicacaoException(WalleValidacoes.ERRO_SERIALIZAR_JSON, e);
		}
	}

	public static <T> T convertJsonStringToObject(String json, TypeReference<T> typeReference) throws AplicacaoException {

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(
					JsonAutoDetect.Visibility.ANY));
			return mapper.readValue(json, typeReference);
		} catch (Exception e) {
			throw new AplicacaoException(WalleValidacoes.ERRO_SERIALIZAR_JSON, e);
		}
	}

}
