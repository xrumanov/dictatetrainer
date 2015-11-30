/**
 * Helper class that allows the equal method to work the right way
 */
package cz.muni.fi.dictatetrainer.commontests.error;

import cz.muni.fi.dictatetrainer.error.model.Error;
import org.mockito.ArgumentMatcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;

public class ErrorArgumentMatcher extends ArgumentMatcher<Error> {

    private Error expected;

    public static Error errorEq(final Error expected) {
        return argThat(new ErrorArgumentMatcher(expected));
    }

    public ErrorArgumentMatcher(final Error expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(final Object actualObj) {
        final Error actual = (Error) actualObj;

        assertThat(actual.getId(), is(equalTo(expected.getId())));
        assertThat(actual.getMistakeCharPosInWord(), is(equalTo(expected.getMistakeCharPosInWord())));
        assertThat(actual.getCorrectChars(), is(equalTo(expected.getCorrectChars())));
        assertThat(actual.getWrittenChars(), is(equalTo(expected.getWrittenChars())));
        assertThat(actual.getCorrectWord(), is(equalTo(expected.getCorrectWord())));
        assertThat(actual.getWrittenWord(), is(equalTo(expected.getWrittenWord())));
        assertThat(actual.getPreviousWord(), is(equalTo(expected.getPreviousWord())));
        assertThat(actual.getNextWord(), is(equalTo(expected.getNextWord())));
        assertThat(actual.getSentence(), is(equalTo(expected.getSentence())));
        assertThat(actual.getLemma(), is(equalTo(expected.getLemma())));
        assertThat(actual.getPosTag(), is(equalTo(expected.getPosTag())));
        assertThat(actual.getErrorType(), is(equalTo(expected.getErrorType())));
        assertThat(actual.getErrorDescription(), is(equalTo(expected.getErrorDescription())));
        assertThat(actual.getErrorPriority(), is(equalTo(expected.getErrorPriority())));
        assertThat(actual.getStudent(), is(equalTo(expected.getStudent())));
        assertThat(actual.getDictate(), is(equalTo(expected.getDictate())));
        assertThat(actual.getTrial(), is(equalTo(expected.getTrial())));

        return true;
    }
}