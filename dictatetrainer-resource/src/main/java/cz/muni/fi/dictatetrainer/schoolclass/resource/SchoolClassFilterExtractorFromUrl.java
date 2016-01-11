package cz.muni.fi.dictatetrainer.schoolclass.resource;

import cz.muni.fi.dictatetrainer.common.resource.AbstractFilterExtractorFromUrl;
import cz.muni.fi.dictatetrainer.schoolclass.model.filter.SchoolClassFilter;

import javax.ws.rs.core.UriInfo;

/**
 * Extract the filter settings from the URL for SchoolClass resource
 */
public class SchoolClassFilterExtractorFromUrl extends AbstractFilterExtractorFromUrl {

    public SchoolClassFilterExtractorFromUrl(final UriInfo uriInfo) {
        super(uriInfo);
    }

    public SchoolClassFilter getFilter() {
        final SchoolClassFilter schoolClassFilter = new SchoolClassFilter();

        schoolClassFilter.setPaginationData(extractPaginationData());

        final String teacherIdStr = getUriInfo().getQueryParameters().getFirst("teacherId");
        if (teacherIdStr != null) {
            schoolClassFilter.setTeacherId(Long.valueOf(teacherIdStr));
        }
        final String schoolIdStr = getUriInfo().getQueryParameters().getFirst("schoolId");
        if (schoolIdStr != null) {
            schoolClassFilter.setSchoolId(Long.valueOf(schoolIdStr));
        }
        return schoolClassFilter;
    }

    @Override
    protected String getDefaultSortField() {
        return "id";
    }

}