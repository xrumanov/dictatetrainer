package cz.muni.fi.dictatetrainer.common.utils;

import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.Set;

/**
 * Generic validation class - validation of entity fields according to constraints set in the entity
 */
public class ValidationUtils {

    public static <T> void validateEntityFields(final Validator validator, final T entity) {
        final Set<ConstraintViolation<T>> errors = validator.validate(entity);
        final Iterator<ConstraintViolation<T>> itErrors = errors.iterator();
        if (itErrors.hasNext()) {
            final ConstraintViolation<T> violation = itErrors.next();
            throw new FieldNotValidException(violation.getPropertyPath().toString(), violation.getMessage());
        }
    }

}