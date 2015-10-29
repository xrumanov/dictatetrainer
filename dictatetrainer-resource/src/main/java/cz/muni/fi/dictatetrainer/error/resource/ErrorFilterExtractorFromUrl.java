package cz.muni.fi.dictatetrainer.error.resource;

import cz.muni.fi.dictatetrainer.common.resource.AbstractFilterExtractorFromUrl;
import cz.muni.fi.dictatetrainer.error.model.filter.ErrorFilter;

import javax.ws.rs.core.UriInfo;

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

        final String dictateIdStr = getUriInfo().getQueryParameters().getFirst("categoryId");
        if (dictateIdStr != null) {
            errorFilter.setDictateId(Long.valueOf(dictateIdStr));
        }

        return errorFilter;
    }

    @Override
    protected String getDefaultSortField() {
        return "id";
    }

}