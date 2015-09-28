/**
 * Helper class that allows the equal method to work the right way
 */
package cz.muni.fi.dictatetrainer.commontests.dictate;

import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import org.mockito.ArgumentMatcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;

public class DictateArgumentMatcher extends ArgumentMatcher<Dictate> {

    private Dictate expected;

    public static Dictate dictateEq(final Dictate expected) {
        return argThat(new DictateArgumentMatcher(expected));
    }

    public DictateArgumentMatcher(final Dictate expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(final Object actualObj) {
        final Dictate actual = (Dictate) actualObj;

        assertThat(actual.getId(), is(equalTo(expected.getId())));
        assertThat(actual.getName(), is(equalTo(expected.getName())));
        assertThat(actual.getDescription(), is(equalTo(expected.getDescription())));
        assertThat(actual.getCategory(), is(equalTo(expected.getCategory())));
        assertThat(actual.getUploader(), is(equalTo(expected.getUploader())));
        assertThat(actual.getTranscript(), is(equalTo(expected.getTranscript())));
        assertThat(actual.getFilename(), is(equalTo(expected.getFilename())));

        return true;
    }
}
