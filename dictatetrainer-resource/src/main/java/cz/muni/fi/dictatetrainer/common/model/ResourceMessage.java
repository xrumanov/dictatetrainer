package cz.muni.fi.dictatetrainer.common.model;

/**
 * Auxiliary class for constructing of JSON messages when there is an Exception thrown
 */
public class ResourceMessage {
    private final String resource;

    private static final String KEY_EXISTENT = "%s.existuje";
    private static final String MESSAGE_EXISTENT = "%s se zadanou hodnotou pole %s již existuje";
    private static final String MESSAGE_INVALID_FIELD = "%s.neplatéPole.%s";
    private static final String KEY_NOT_FOUND = "%s.nenalezen";
    private static final String MESSAGE_NOT_FOUND = "%s nebyl nalezen";
    private static final String NOT_FOUND = "Nebyl nalezen";

    public ResourceMessage(final String resource) {
        this.resource = resource;
    }

    public String getKeyOfResourceExistent() {
        return String.format(KEY_EXISTENT, resource);
    }

    public String getMessageOfResourceExistent(final String fieldsNames) {
        return String.format(MESSAGE_EXISTENT, resource, fieldsNames);
    }

    public String getKeyOfInvalidField(final String invalidField) {
        return String.format(MESSAGE_INVALID_FIELD, resource, invalidField);
    }

    public String getKeyOfResourceNotFound() {
        return String.format(KEY_NOT_FOUND, resource);
    }

    public String getMessageOfResourceNotFound() {
        return String.format(MESSAGE_NOT_FOUND, resource);
    }

    public String getMessageNotFound() {
        return NOT_FOUND;
    }

}