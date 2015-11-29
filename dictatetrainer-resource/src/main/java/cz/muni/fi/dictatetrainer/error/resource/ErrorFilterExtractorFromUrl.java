package cz.muni.fi.dictatetrainer.error.resource;

import cz.muni.fi.dictatetrainer.common.resource.AbstractFilterExtractorFromUrl;
import cz.muni.fi.dictatetrainer.error.model.filter.ErrorFilter;

import javax.ws.rs.core.UriInfo;

/**
 * Extract the filter settings from the URL for Error resource
 */
public class ErrorFilterExtractorFromUrl extends AbstractFilterExtractorFromUrl {

    public ErrorFilterExtractorFromUrl(final UriInfo uriInfo) {
        super(uriInfo);
    }

    public ErrorFilter getFilter() {
        final ErrorFilter errorFilter = new ErrorFilter();

        errorFilter.setPaginationData(extractPaginationData());

        final String studentIdStr = getUriInfo().getQueryParameters().getFirst("studentId");
        if (studentIdStr != null) {
            errorFilter.setStudentId(Long.valueOf(studentIdStr));
        }

        final String dictateIdStr = getUriInfo().getQueryParameters().getFirst("dictateId");
        if (dictateIdStr != null) {
            errorFilter.setDictateId(Long.valueOf(dictateIdStr));
        }

        final String trialIdStr = getUriInfo().getQueryParameters().getFirst("trialId");
        if (trialIdStr != null) {
            errorFilter.setTrialId(Long.valueOf(trialIdStr));
        }

        return errorFilter;
    }

    @Override
    protected String getDefaultSortField() {
        return "id";
    }

}