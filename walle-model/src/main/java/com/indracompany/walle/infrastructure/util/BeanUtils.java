package com.indracompany.walle.infrastructure.util;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Embedded;
import javax.validation.constraints.Null;

import org.apache.commons.lang3.ArrayUtils;

public class BeanUtils {

    public static void copyProperties(Object source, Object target) {
        deepCopyProperties(source, target);
        org.springframework.beans.BeanUtils.copyProperties(source, target, getNullProperties(source));
    }

    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        deepCopyProperties(source, target);
        org.springframework.beans.BeanUtils.copyProperties(source, target,
                ArrayUtils.addAll(getNullProperties(source), ignoreProperties));
    }

    private static void deepCopyProperties(Object source, Object target) {
        List<Field> sourceFields = FieldUtils.getAllFields(source.getClass());

        for (Field sourceField : sourceFields) {
            Embedded embedded = sourceField.getAnnotation(Embedded.class);

            if (embedded != null) {
                sourceField.setAccessible(true);

                try {
                    Field targetField = FieldUtils.getField(target.getClass(), sourceField.getName());
                    targetField.setAccessible(true);
                    copyProperties(sourceField.get(source), targetField.get(target));
                    sourceField.set(source, targetField.get(target));
                } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException exception) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    private static String[] getNullProperties(Object source) {
        return FieldUtils.getAllFields(source.getClass()).stream().filter(field -> StreamUtils.propagate(() -> {
            Null nullConstraint = field.getAnnotation(Null.class);

            if (nullConstraint != null) {
                return false;
            }

            field.setAccessible(true);

            return field.get(source) == null;
        })).map(Field::getName).toArray(String[]::new);
    }

}
