package cz.muni.fi.dictatetrainer.commontests.school;

import cz.muni.fi.dictatetrainer.school.model.School;
import org.junit.Ignore;

import java.util.Arrays;
import java.util.List;

/**
 * Prepared School entities used for test purposes
 */
@Ignore
public class SchoolsForTestRepository {

    private SchoolsForTestRepository() {
    }

    public static School school1() {
        final School school = new School();
        school.setName("7. ZS, Trencin");

        return school;
    }

    public static School school2() {
        final School school = new School();
        school.setName("4. ZS, Trencin");

        return school;
    }

    public static School school3() {
        final School school = new School();
        school.setName("4. ZS, Brno");

        return school;
    }


    public static School schoolWithId(final School school, final Long id) {
        school.setId(id);
        return school;
    }

    public static List<School> allSchools() {
        return Arrays.asList(school1(), school2(), school3());
    }
}