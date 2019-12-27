package com.indracompany.walle.infrastructure.persistence.hibernate.specification;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.indracompany.walle.infrastructure.annotation.specification.SpecificationEntity;
import com.indracompany.walle.infrastructure.annotation.specification.SpecificationField;
import com.indracompany.walle.infrastructure.util.FieldUtils;

@Component
public class SpecificationFactory<T> {

    private CriteriaBuilder criteriaBuilder;

    private Root<T> root;

    private List<Predicate> predicates;

    public Specification<T> create(Object data) {
        return (root, query, criteriaBuilder) -> {
            try {
                this.criteriaBuilder = criteriaBuilder;
                this.root = root;
                predicates = new ArrayList<>();

                SpecificationEntity specificationEntity = data.getClass().getAnnotation(SpecificationEntity.class);
                List<Field> dataFields = FieldUtils.getAllFields(data.getClass());
                List<Field> entityFields = FieldUtils.getAllFields(specificationEntity.value());

                for (Field field : dataFields) {
                    field.setAccessible(true);
                    Object value = field.get(data);

                    if (hasEntityThisProperty(entityFields, field, value)) {
                        addPredicate(field, field.get(data));
                    }
                }

                return this.criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            } catch (IllegalArgumentException | IllegalAccessException exception) {
                exception.printStackTrace();
            }

            return null;
        };
    }

    private boolean hasEntityThisProperty(List<Field> entityFields, Field field, Object value) {
        return value != null && entityFields.stream().anyMatch(ef -> hasEntityFieldEqualProperty(field, ef));
    }

    private boolean hasEntityFieldEqualProperty(Field field, Field ef) {
        if (!hasSpecificationFieldAndProperty(field)) {
            return ef.getName().equals(field.getName());
        }

        SpecificationField specificationField = field.getAnnotation(SpecificationField.class);
        String property = specificationField.property().contains(".")
                ? specificationField.property().substring(0, specificationField.property().indexOf('.'))
                : specificationField.property();
        return ef.getName().equals(property);

    }

    private void addPredicate(Field field, Object value) {
        if (field.isAnnotationPresent(SpecificationField.class)) {
            SpecificationField specificationField = field.getAnnotation(SpecificationField.class);
            String property = specificationField.property() == null || specificationField.property().isEmpty()
                    ? field.getName()
                    : specificationField.property();
            SpecificationOperation operation = specificationField.operation();
            Predicate predicate = null;

            if (value instanceof Number) {
                predicate = buildPredicate(property, Integer.parseInt(value.toString()), operation);
            } else if (value instanceof Boolean) {
                predicate = buildPredicate(property, (Boolean) value, operation);
            } else if (value instanceof LocalDate) {
                predicate = buildPredicate(property, LocalDate.parse(value.toString()), operation);
            } else if (value instanceof LocalDateTime) {
                predicate = buildPredicate(property, LocalDateTime.parse(value.toString()), operation);
            } else if (value instanceof Enum<?>) {
                predicate = buildPredicate(property, (Enum<?>) value);
            } else {
                predicate = buildPredicate(property, value.toString(), operation);
            }

            predicates.add(predicate);
        }
    }

    @SuppressWarnings("unchecked")
    private Predicate buildPredicate(String property, Integer value, SpecificationOperation operation) {
        Path<Integer> path = (Path<Integer>) generatePath(property);
        switch (operation) {
        case EQUAL:
            return criteriaBuilder.equal(path, value);
        case GREATER_THAN:
            return criteriaBuilder.greaterThan(path, value);
        case LESS_THAN:
            return criteriaBuilder.lessThan(path, value);
        case GREATER_THAN_OR_EQUAL:
            return criteriaBuilder.greaterThan(path, value);
        case LESS_THAN_OR_EQUAL:
            return criteriaBuilder.lessThanOrEqualTo(path, value);
        default:
            return criteriaBuilder.equal(path, value);
        }
    }

    @SuppressWarnings("unchecked")
    private Predicate buildPredicate(String property, Boolean value, SpecificationOperation operation) {
        Path<Boolean> path = (Path<Boolean>) generatePath(property);
        if (operation.equals(SpecificationOperation.EXISTS)) {
            return value.booleanValue() ? criteriaBuilder.isNotNull(path)
                    : criteriaBuilder.isNull(path);
        }

        return criteriaBuilder.equal(path, value);
    }

    @SuppressWarnings("unchecked")
    private Predicate buildPredicate(String property, Enum<?> value) {
        Path<Enum<?>> path = (Path<Enum<?>>) generatePath(property);
        return criteriaBuilder.equal(path, value);
    }
    
    @SuppressWarnings("unchecked")
    private Predicate buildPredicate(String property, LocalDate value, SpecificationOperation operation) {
        Path<LocalDate> path = (Path<LocalDate>) generatePath(property);
        switch (operation) {
        case EQUAL:
            return criteriaBuilder.equal(path, value);
        case GREATER_THAN:
            return criteriaBuilder.greaterThan(path, value);
        case LESS_THAN:
            return criteriaBuilder.lessThan(path, value);
        case GREATER_THAN_OR_EQUAL:
            return criteriaBuilder.greaterThanOrEqualTo(path, value);
        case LESS_THAN_OR_EQUAL:
            return criteriaBuilder.lessThanOrEqualTo(path, value);
        default:
            return criteriaBuilder.equal(path, value);
        }
    }

    @SuppressWarnings("unchecked")
    private Predicate buildPredicate(String property, LocalDateTime value, SpecificationOperation operation) {
        Path<LocalDateTime> path = (Path<LocalDateTime>) generatePath(property);
        switch (operation) {
        case EQUAL:
            return criteriaBuilder.equal(path, value);
        case GREATER_THAN:
            return criteriaBuilder.greaterThan(path, value);
        case LESS_THAN:
            return criteriaBuilder.lessThan(path, value);
        case GREATER_THAN_OR_EQUAL:
            return criteriaBuilder.greaterThanOrEqualTo(path, value);
        case LESS_THAN_OR_EQUAL:
            return criteriaBuilder.lessThanOrEqualTo(path, value);
        default:
            return criteriaBuilder.equal(path, value);
        }
    }

    @SuppressWarnings("unchecked")
    private Predicate buildPredicate(String property, String value, SpecificationOperation operation) {
        Path<?> path = generatePath(property);
        switch (operation) {
        case EQUAL:
            return criteriaBuilder.equal(path, value);
        case EQUAL_IGNORE_CASE:
            return criteriaBuilder.equal(criteriaBuilder.lower((Path<String>) path), value.toLowerCase());
        case LIKE_IGNORE_CASE:
            return criteriaBuilder.like(criteriaBuilder.lower((Path<String>) path), "%" + value.toLowerCase() + "%");
        default:
            return criteriaBuilder.equal(path, value);
        }
    }
    
    private Path<?> generatePath(String property) {
        Path<?> path = null;
        List<String> list = new ArrayList<>(Arrays.asList(property.split("\\.")));
        for (String p : list) {
            if (path == null) {
                path = root.get(p);
            } else {

                path = path.get(p);
            }
        }
        return path;
    }
    
    private boolean hasSpecificationFieldAndProperty(Field field) {
        return field.isAnnotationPresent(SpecificationField.class) && field.getAnnotation(SpecificationField.class).property() != null;
    }

}
