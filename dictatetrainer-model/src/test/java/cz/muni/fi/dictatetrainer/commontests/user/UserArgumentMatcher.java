/**
 * Helper class that allows the equal method to work the right way
 */
package cz.muni.fi.dictatetrainer.commontests.user;

import cz.muni.fi.dictatetrainer.user.model.User;
import org.junit.Ignore;
import org.mockito.ArgumentMatcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;

@Ignore
public class UserArgumentMatcher extends ArgumentMatcher<User> {
    private User expectedUser;

    public static User userEq(final User expectedUser) {
        return argThat(new UserArgumentMatcher(expectedUser));
    }

    public UserArgumentMatcher(final User expectedUser) {
        this.expectedUser = expectedUser;
    }

    @Override
    public boolean matches(final Object argument) {
        final User actualUser = (User) argument;

        assertThat(actualUser.getId(), is(equalTo(expectedUser.getId())));
        assertThat(actualUser.getName(), is(equalTo(expectedUser.getName())));
        assertThat(actualUser.getEmail(), is(equalTo(expectedUser.getEmail())));
        assertThat(actualUser.getPassword(), is(equalTo(expectedUser.getPassword())));

        return true;
    }

}
