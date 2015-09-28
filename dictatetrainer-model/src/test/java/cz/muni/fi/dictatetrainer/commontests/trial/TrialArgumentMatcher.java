/**
 * Helper class that allows the equal method to work the right way
 */
package cz.muni.fi.dictatetrainer.commontests.trial;

import cz.muni.fi.dictatetrainer.trial.model.Trial;
import org.mockito.ArgumentMatcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;

public class TrialArgumentMatcher extends ArgumentMatcher<Trial> {

    private Trial expected;

    public static Trial orderEq(final Trial expected) {
        return argThat(new TrialArgumentMatcher(expected));
    }

    public TrialArgumentMatcher(final Trial expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(final Object actualObj) {
        final Trial actual = (Trial) actualObj;

        assertThat(actual.getId(), is(equalTo(expected.getId())));
        assertThat(actual.getStudent(), is(equalTo(expected.getStudent())));
        assertThat(actual.getDictate(), is(equalTo(expected.getDictate())));
        assertThat(actual.getTrialText(), is(equalTo(expected.getTrialText())));
        assertThat(actual.getPerformed(), is(equalTo(expected.getPerformed())));

        return true;
    }

}
