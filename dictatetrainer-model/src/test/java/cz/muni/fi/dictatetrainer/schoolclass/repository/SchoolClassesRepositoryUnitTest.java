package cz.muni.fi.dictatetrainer.schoolclass.repository;

import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.commontests.utils.TestBaseRepository;
import cz.muni.fi.dictatetrainer.schoolclass.model.SchoolClass;
import cz.muni.fi.dictatetrainer.schoolclass.model.filter.SchoolClassFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static cz.muni.fi.dictatetrainer.commontests.school.SchoolsForTestRepository.allSchools;
import static cz.muni.fi.dictatetrainer.commontests.schoolclass.SchoolClassesForTestRepository.normalizeDependencies;
import static cz.muni.fi.dictatetrainer.commontests.schoolclass.SchoolClassesForTestRepository.schoolClass1;
import static cz.muni.fi.dictatetrainer.commontests.user.UsersForTestRepository.allUsers;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class SchoolClassesRepositoryUnitTest extends TestBaseRepository {

    private SchoolClassRepository schoolClassRepository;

    @Before
    public void initTestCase() {
        initializeTestDB();

        schoolClassRepository = new SchoolClassRepository();
        schoolClassRepository.em = em;

        loadSchools();
    }

    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    @Test
    public void addAnSchoolClassAndFindIt() {
        final Long schoolClassAddedId = dbCommandExecutor.executeCommand(() -> {
            return schoolClassRepository.add(normalizeDependencies(schoolClass1(), em)).getId();
        });
        assertThat(schoolClassAddedId, is(notNullValue()));

        final SchoolClass schoolClassAdded = schoolClassRepository.findById(schoolClassAddedId);
        assertActualSchoolClassWithExpectedSchoolClass(schoolClassAdded, normalizeDependencies(schoolClass1(), em));
    }

    @Test
    public void findSchoolClassByIdNotFound() {
        final SchoolClass schoolClass = schoolClassRepository.findById(999L);
        assertThat(schoolClass, is(nullValue()));
    }


    @Test
    public void existsById() {
        final Long schoolClassAddedId = dbCommandExecutor.executeCommand(() -> {
            return schoolClassRepository.add(normalizeDependencies(schoolClass1(), em)).getId();
        });
        assertThat(schoolClassAddedId, is(notNullValue()));

        assertThat(schoolClassRepository.existsById(schoolClassAddedId), is(equalTo(true)));
        assertThat(schoolClassRepository.existsById(999l), is(equalTo(false)));
    }

    @Test
    public void findByFilterNoFilter() {
        loadForFindByFilter();

        final PaginatedData<SchoolClass> schoolClasses = schoolClassRepository.findByFilter(new SchoolClassFilter());
        assertThat(schoolClasses.getNumberOfRows(), is(equalTo(1)));
    }

    @Test
    public void findByFilterFilteringByTeacherId() {
        loadForFindByFilter();

        final SchoolClassFilter filter = new SchoolClassFilter();
        Long teacherId = normalizeDependencies(schoolClass1(), em).getTeacher().getId();
        filter.setTeacherId(teacherId);

        final PaginatedData<SchoolClass> schoolClasses = schoolClassRepository.findByFilter(filter);
        assertThat(filter.getTeacherId(), is(notNullValue()));
        assertThat(filter.getTeacherId(), is(equalTo(teacherId)));
        assertThat(schoolClasses.getNumberOfRows(), is(equalTo(1)));
    }

    private void loadForFindByFilter() {
        final SchoolClass schoolClass1 = normalizeDependencies(schoolClass1(), em);

        dbCommandExecutor.executeCommand(() -> {
            schoolClassRepository.add(schoolClass1);
            return null;
        });
    }

    private void assertActualSchoolClassWithExpectedSchoolClass(final SchoolClass actualSchoolClass, final SchoolClass expectedSchoolClass) {
        assertThat(expectedSchoolClass.getName(), is(notNullValue()));
        assertThat(actualSchoolClass.getTeacher(), is(equalTo(expectedSchoolClass.getTeacher())));
        assertThat(actualSchoolClass.getSchool(), is(equalTo(expectedSchoolClass.getSchool())));
    }

    private void loadSchools() {
        dbCommandExecutor.executeCommand(() -> {
            allUsers().forEach(em::persist);
            allSchools().forEach(em::persist);
            return null;
        });
    }
}
