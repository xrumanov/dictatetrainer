package cz.muni.fi.dictatetrainer.commontests.schoolclass;

import cz.muni.fi.dictatetrainer.schoolclass.model.SchoolClass;
import org.mockito.ArgumentMatcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;

/**
 * Helper class that allows the equal method to work the right way
 */
public class SchoolClassesArgumentMatcher extends ArgumentMatcher<SchoolClass> {

    private SchoolClass expected;

    public static SchoolClass schoolClassEq(final SchoolClass expected) {
        return argThat(new SchoolClassesArgumentMatcher(expected));
    }

    public SchoolClassesArgumentMatcher(final SchoolClass expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(final Object actualObj) {
        final SchoolClass actual = (SchoolClass) actualObj;

        assertThat(actual.getId(), is(equalTo(expected.getId())));
        assertThat(actual.getName(), is(equalTo(expected.getName())));
        assertThat(actual.getSchool(), is(equalTo(expected.getSchool())));
        assertThat(actual.getTeacher(), is(equalTo(expected.getTeacher())));

        return true;
    }

}