/**
 * Allows filtering of returned results by user type and by substring in name(sorting by name)
 */
package cz.muni.fi.dictatetrainer.user.model.filter;

import cz.muni.fi.dictatetrainer.common.model.filter.GenericFilter;
import cz.muni.fi.dictatetrainer.user.model.User;

/**
 * Filtering entity User by given parameters
 *
 * Available params:
 *  name: shows only users with given name
 *  userType:  shows only users that are STUDENT or only TEACHER
 *
 */
public class UserFilter extends GenericFilter {
	private String name;
	private User.UserType userType;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public User.UserType getUserType() {
		return userType;
	}

	public void setUserType(final User.UserType userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return "UserFilter [name=" + name + ", userType=" + userType + ", toString()=" + super.toString() + "]";
	}

}