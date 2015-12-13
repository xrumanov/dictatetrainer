package cz.muni.fi.dictatetrainer.user.resource;

import cz.muni.fi.dictatetrainer.common.resource.AbstractFilterExtractorFromUrl;
import cz.muni.fi.dictatetrainer.user.model.User;
import cz.muni.fi.dictatetrainer.user.model.filter.UserFilter;

import javax.ws.rs.core.UriInfo;

/**
 * Extract the filter settings from the URL for User resource
 */
public class UserFilterExtractorFromUrl extends AbstractFilterExtractorFromUrl {

    public UserFilterExtractorFromUrl(final UriInfo uriInfo) {
        super(uriInfo);
    }

    public UserFilter getFilter() {
        final UserFilter userFilter = new UserFilter();
        userFilter.setPaginationData(extractPaginationData());
        userFilter.setName(getUriInfo().getQueryParameters().getFirst("name"));

        final String schoolClassIdStr = getUriInfo().getQueryParameters().getFirst("schoolClassId");
        if (schoolClassIdStr != null) {
            userFilter.setSchoolClassId(Long.valueOf(schoolClassIdStr));
        }
        final String userType = getUriInfo().getQueryParameters().getFirst("type");
        if (userType != null) {
            userFilter.setUserType(User.UserType.valueOf(userType));
        }

        return userFilter;
    }

    @Override
    protected String getDefaultSortField() {
        return "name";
    }

}