package cz.muni.fi.dictatetrainer.corrector.exception;

import javax.ejb.ApplicationException;

/**
 * Exception thrown when one of the fields defined in an Entity is invalid according to Validator
 */
@ApplicationException
public class FieldNotValidException extends RuntimeException {
    private static final long serialVersionUID = 4525821332583716666L;

    private final String fieldName;

    public FieldNotValidException(final String fieldName, final String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String toString() {
        return "FieldNotValidException [fieldName=" + fieldName + "]";
    }

}