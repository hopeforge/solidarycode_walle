package com.indracompany.walle.domain.shared;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.extern.slf4j.Slf4j;

/**
 * @author eder
 *
 */
@Slf4j
public class GenericEntity<I> implements Persistable<I>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Transient
	private Boolean persisted = Boolean.TRUE;

	/**
	 * Objetivo: Dada uma entidade, obter o valor contido no atributo marcado como
	 * ID (@Id).
	 * 
	 * Funcionamento: Através reflection obtém a entidade marcada com a annotation e
	 * o seu respectivo valor
	 * 
	 * @param entidade
	 * @return Serializable
	 */
	@SuppressWarnings("unchecked")
	@Override
	public I getId() {
		I retorno = null;
		try {
			retorno = (I) getIdField().get(Long.class);
		} catch (Exception e) {
			log.error("erro ao obter getId da entidade ", e);
		}
		return retorno;
	}

	@JsonIgnore
	public Field getIdField() {
		Field retorno = null;
		Class<?> actualClass = getClass();

		try {
			do {

				for (Field fieldSequenceId : actualClass.getDeclaredFields()) {
					fieldSequenceId.setAccessible(true);
					if (checkIdField(fieldSequenceId)) {
						retorno = fieldSequenceId;
						actualClass = actualClass.getSuperclass();
					}
				}
			} while (Objects.isNull(retorno) && !Object.class.equals(actualClass));
		} catch (Exception e) {
			log.error("erro ao obter getIdField da entidade ", e);
		}
		return retorno;
	}

	/**
	 * @author eder Obtem o valor do atributo mapeado com a anotação @Id
	 *         ou @EmbeddedId
	 * @param entidade
	 * @param fields
	 * @return Field
	 */
	private static Boolean checkIdField(Field field) {
		return !Objects.isNull(field.getAnnotation(Id.class));
	}

	public Set<ConstraintViolation<GenericEntity<I>>> validationsConstraintsFails() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		return validator.validate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof GenericEntity)) {
			return false;
		}
		GenericEntity<?> other = (GenericEntity<?>) obj;
		if (other.getId() == null
				|| (Number.class.isInstance(other.getId()) && ((Number) other.getId()).longValue() == 0)
				|| (String.class.isInstance(other.getId()) && ((String) other.getId()).isEmpty())) {
			return false;
		} else if (this.getId() == null) {
			if (other.getId() != null) {
				return false;
			}
		} else if (!this.getId().equals(other.getId())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		return result;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return !persisted;
	}

	@JsonIgnore
	public Boolean getPersisted() {
		return persisted;
	}

	public void setPersisted(Boolean persisted) {
		this.persisted = persisted;
	}

}