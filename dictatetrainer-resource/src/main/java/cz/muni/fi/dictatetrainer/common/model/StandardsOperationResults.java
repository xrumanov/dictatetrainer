package cz.muni.fi.dictatetrainer.common.model;

import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;

/**
 * Auxiliary method for using ResourceMessage class
 */
public final class StandardsOperationResults {

    private StandardsOperationResults() {
    }

    public static OperationResult getOperationResultExistent(final ResourceMessage resourceMessage,
                                                             final String fieldsNames) {
        return OperationResult.error(resourceMessage.getKeyOfResourceExistent(),
                resourceMessage.getMessageOfResourceExistent(fieldsNames));
    }

    public static OperationResult getOperationResultInvalidField(final ResourceMessage resourceMessage,
                                                                 final FieldNotValidException ex) {
        return OperationResult.error(resourceMessage.getKeyOfInvalidField(ex.getFieldName()), ex.getMessage());
    }

    public static OperationResult getOperationResultNotFound(final ResourceMessage resourceMessage) {
        return OperationResult.error(resourceMessage.getKeyOfResourceNotFound(),
                resourceMessage.getMessageOfResourceNotFound());
    }


    public static OperationResult getOperationResultDependencyNotFound(final ResourceMessage resourceMessage,
                                                                       final String dependencyField) {
        return OperationResult.error(resourceMessage.getKeyOfInvalidField(dependencyField),
                resourceMessage.getMessageNotFound());
    }

}