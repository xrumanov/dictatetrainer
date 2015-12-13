package cz.muni.fi.dictatetrainer.school.repository;

import cz.muni.fi.dictatetrainer.commontests.utils.TestBaseRepository;
import cz.muni.fi.dictatetrainer.school.model.School;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static cz.muni.fi.dictatetrainer.commontests.school.SchoolsForTestRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class SchoolRepositoryUnitTest extends TestBaseRepository {

    private SchoolRepository schoolRepository;

    @Before
    public void initTestCase() {
        initializeTestDB();

        schoolRepository = new SchoolRepository();
        schoolRepository.em = em;
    }

    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    @Test
    public void addSchoolAndFindIt() {
        final Long schoolAddedId = dbCommandExecutor.executeCommand(() -> {
            return schoolRepository.add(school1()).getId();
        });

        assertThat(schoolAddedId, is(notNullValue()));

        final School school = schoolRepository.findById(schoolAddedId);
        assertThat(school, is(notNullValue()));
        assertThat(school.getName(), is(equalTo(school1().getName())));
    }

    @Test
    public void findSchoolByIdNotFound() {
        final School school = schoolRepository.findById(999L);
        assertThat(school, is(nullValue()));
    }

    @Test
    public void findSchoolByIdNullId() {
        final School school = schoolRepository.findById(null);
        assertThat(school, is(nullValue()));
    }

    @Test
    public void updateSchool() {
        final Long schoolAddedId = dbCommandExecutor.executeCommand(() -> {
            return schoolRepository.add(school1()).getId();
        });

        final School schoolAfterAdd = schoolRepository.findById(schoolAddedId);
        assertThat(schoolAfterAdd.getName(), is(equalTo(school1().getName())));

        schoolAfterAdd.setName(school2().getName());
        dbCommandExecutor.executeCommand(() -> {
            schoolRepository.update(schoolAfterAdd);
            return null;
        });

        final School schoolAfterUpdate = schoolRepository.findById(schoolAddedId);
        assertThat(schoolAfterUpdate.getName(), is(equalTo(school2().getName())));
    }

    @Test
    public void findAllCategories() {
        dbCommandExecutor.executeCommand(() -> {
            allSchools().forEach(schoolRepository::add);
            return null;
        });

        final List<School> categories = schoolRepository.findAll("name");
        assertThat(categories.size(), is(equalTo(3)));
        assertThat(categories.get(0).getName(), is(equalTo(school3().getName())));
        assertThat(categories.get(1).getName(), is(equalTo(school2().getName())));
        assertThat(categories.get(2).getName(), is(equalTo(school1().getName())));
    }

    @Test
    public void alreadyExistsWhileAdding() {
        dbCommandExecutor.executeCommand(() -> {
            schoolRepository.add(school1());
            return null;
        });

        assertThat(schoolRepository.alreadyExists(school1()), is(equalTo(true)));
        assertThat(schoolRepository.alreadyExists(school2()), is(equalTo(false)));
    }

    @Test
    public void schoolWithIdAlreadyExists() {
        final School school2 = dbCommandExecutor.executeCommand(() -> {
            schoolRepository.add(school2());
            return schoolRepository.add(school1());
        });

        assertThat(schoolRepository.alreadyExists(school2), is(equalTo(false)));

        school2.setName(school2().getName());
        assertThat(schoolRepository.alreadyExists(school2), is(equalTo(true)));

        school2.setName(school3().getName());
        assertThat(schoolRepository.alreadyExists(school2), is(equalTo(false)));
    }

    @Test
    public void existsById() {
        final Long schoolAddedId = dbCommandExecutor.executeCommand(() -> {
            return schoolRepository.add(school1()).getId();
        });

        assertThat(schoolRepository.existsById(schoolAddedId), is(equalTo(true)));
        assertThat(schoolRepository.existsById(999L), is(equalTo(false)));
    }

}
