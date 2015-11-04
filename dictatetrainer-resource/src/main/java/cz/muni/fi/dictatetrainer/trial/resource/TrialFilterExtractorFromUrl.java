package cz.muni.fi.dictatetrainer.trial.resource;

import cz.muni.fi.dictatetrainer.common.resource.AbstractFilterExtractorFromUrl;
import cz.muni.fi.dictatetrainer.common.utils.DateUtils;
import cz.muni.fi.dictatetrainer.trial.model.filter.TrialFilter;

import javax.ws.rs.core.UriInfo;

/**
 * Extract the filter settings from the URL for Trial resource
 */
public class TrialFilterExtractorFromUrl extends AbstractFilterExtractorFromUrl {

    public TrialFilterExtractorFromUrl(final UriInfo uriInfo) {
        super(uriInfo);
    }

    public TrialFilter getFilter() {
        final TrialFilter trialFilter = new TrialFilter();

        trialFilter.setPaginationData(extractPaginationData());

        final String startDateStr = getUriInfo().getQueryParameters().getFirst("startDate");
        if (startDateStr != null) {
            trialFilter.setStartDate(DateUtils.getAsDateTime(startDateStr));
        }

        final String endDateStr = getUriInfo().getQueryParameters().getFirst("endDate");
        if (endDateStr != null) {
            trialFilter.setEndDate(DateUtils.getAsDateTime(endDateStr));
        }

        final String studentIdStr = getUriInfo().getQueryParameters().getFirst("studentId");
        if (studentIdStr != null) {
            trialFilter.setStudentId(Long.valueOf(studentIdStr));
        }

        final String dictateIdStr = getUriInfo().getQueryParameters().getFirst("dictateId");
        if (dictateIdStr != null) {
            trialFilter.setDictateId(Long.valueOf(dictateIdStr));
        }

        return trialFilter;
    }

    @Override
    protected String getDefaultSortField() {
        return "-performed";
    }

}