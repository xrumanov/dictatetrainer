package cz.muni.fi.dictatetrainer.commontests.schoolclass;

import cz.muni.fi.dictatetrainer.school.model.School;
import cz.muni.fi.dictatetrainer.schoolclass.model.SchoolClass;
import cz.muni.fi.dictatetrainer.user.model.Teacher;
import org.junit.Ignore;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static cz.muni.fi.dictatetrainer.commontests.school.SchoolsForTestRepository.school1;
import static cz.muni.fi.dictatetrainer.commontests.school.SchoolsForTestRepository.school2;
import static cz.muni.fi.dictatetrainer.commontests.user.UsersForTestRepository.admin;
import static cz.muni.fi.dictatetrainer.commontests.utils.TestRepositoryUtils.findByPropertyNameAndValue;

/**
 * Prepared SchoolClass entities used for test purposes
 */
@Ignore
public class SchoolClassesForTestRepository {

    private SchoolClassesForTestRepository() {
    }

    public static SchoolClass schoolClass1() {
        final SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("4A");
        schoolClass.setSchool(school1());
        schoolClass.setTeacher(admin());

        return schoolClass;
    }

    public static SchoolClass schoolClass2() {
        final SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("2A");
        schoolClass.setSchool(school2());
        schoolClass.setTeacher(admin());

        return schoolClass;
    }

    public static SchoolClass schoolClassWithId(final SchoolClass schoolClass, final Long id) {
        schoolClass.setId(id);
        return schoolClass;
    }

    public static List<SchoolClass> allSchoolClasses() {
        return Arrays.asList(schoolClass1(), schoolClass2());
    }

    public static SchoolClass normalizeDependencies(final SchoolClass schoolClass, final EntityManager em) {
        schoolClass.setSchool(findByPropertyNameAndValue(em, School.class, "name", schoolClass.getSchool().getName()));
        schoolClass.setTeacher(findByPropertyNameAndValue(em, Teacher.class, "email", schoolClass.getTeacher().getEmail()));

        return schoolClass;
    }
}
