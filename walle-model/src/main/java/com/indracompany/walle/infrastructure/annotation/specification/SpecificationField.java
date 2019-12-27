package com.indracompany.walle.infrastructure.annotation.specification;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.indracompany.walle.infrastructure.persistence.hibernate.specification.SpecificationOperation;

@Retention(RUNTIME)
@Target({ FIELD })
public @interface SpecificationField {

    public String property() default "";

    public SpecificationOperation operation() default SpecificationOperation.EQUAL;

}
