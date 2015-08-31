package cz.muni.fi.dictatetrainer.user.resource;

import javax.ws.rs.core.UriInfo;

import cz.muni.fi.dictatetrainer.common.resource.AbstractFilterExtractorFromUrl;
import cz.muni.fi.dictatetrainer.user.model.filter.UserFilter;
import cz.muni.fi.dictatetrainer.user.model.User;

public class UserFilterExtractorFromUrl extends AbstractFilterExtractorFromUrl {

	public UserFilterExtractorFromUrl(final UriInfo uriInfo) {
		super(uriInfo);
	}

	public UserFilter getFilter() {
		final UserFilter userFilter = new UserFilter();
		userFilter.setPaginationData(extractPaginationData());
		userFilter.setName(getUriInfo().getQueryParameters().getFirst("name"));
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