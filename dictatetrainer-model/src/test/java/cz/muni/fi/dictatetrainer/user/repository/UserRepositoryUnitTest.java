package cz.muni.fi.dictatetrainer.user.repository;

import static cz.muni.fi.dictatetrainer.commontests.school.SchoolsForTestRepository.*;
import static cz.muni.fi.dictatetrainer.commontests.schoolclass.SchoolClassesForTestRepository.*;
import static cz.muni.fi.dictatetrainer.commontests.user.UsersForTestRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import cz.muni.fi.dictatetrainer.schoolclass.model.SchoolClass;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.model.filter.PaginationData;
import cz.muni.fi.dictatetrainer.common.model.filter.PaginationData.OrderMode;
import cz.muni.fi.dictatetrainer.commontests.utils.TestBaseRepository;
import cz.muni.fi.dictatetrainer.user.model.User;
import cz.muni.fi.dictatetrainer.user.model.User.UserType;
import cz.muni.fi.dictatetrainer.user.model.filter.UserFilter;

public class UserRepositoryUnitTest extends TestBaseRepository {
    private UserRepository userRepository;

    @Before
    public void initTestCase() {
        initializeTestDB();

        userRepository = new UserRepository();
        userRepository.em = em;

        loadSchoolClasses();
    }

    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    @Test
    public void addStudentAndFindHim() {
        final Long userAddedId = dbCommandExecutor.executeCommand(() -> {
            return userRepository.add(normalizeDependencies(mrkvicka(), em)).getId();
        });
        assertThat(userAddedId, is(notNullValue()));

        final User user = userRepository.findById(userAddedId);
        assertUser(user, normalizeDependencies(mrkvicka(), em), UserType.STUDENT);
    }

    @Test
    public void findUserByIdNotFound() {
        final User user = userRepository.findById(999L);
        assertThat(user, is(nullValue()));
    }

    @Test
    public void updateStudent() {
        final Long userAddedId = dbCommandExecutor.executeCommand(() -> {
            return userRepository.add(normalizeDependencies(mrkvicka(), em)).getId();
        });
        assertThat(userAddedId, is(notNullValue()));

        final User user = userRepository.findById(userAddedId);
        assertThat(user.getName(), is(equalTo(mrkvicka().getName())));

        user.setName("Updated name");
        dbCommandExecutor.executeCommand(() -> {
            userRepository.update(user);
            return null;
        });

        final User userAfterUpdate = userRepository.findById(userAddedId);
        assertThat(userAfterUpdate.getName(), is(equalTo("Updated name")));
    }

    @Test
    public void findUserByEmail() {
        dbCommandExecutor.executeCommand(() -> {
            return userRepository.add(normalizeDependencies(mrkvicka(), em));
        });

        final User user = userRepository.findByEmail(mrkvicka().getEmail());
        SchoolClass sc = user.getSchoolClass();
        sc.setId(null);
        user.setSchoolClass(sc);
        assertUser(user, mrkvicka(), UserType.STUDENT);
    }

    @Test
    public void findUserByEmailNotFound() {
        final User user = userRepository.findByEmail(mrkvicka().getEmail());
        assertThat(user, is(nullValue()));
    }

    @Test
    public void findUserByFilterPagingOrderingByNameDesc() {
        loadDataForFindByFilter();

        UserFilter userFilter = new UserFilter();
        userFilter.setPaginationData(new PaginationData(0, 2, "name", OrderMode.DESCENDING));

        PaginatedData<User> result = userRepository.findByFilter(userFilter);
        assertThat(result.getNumberOfRows(), is(equalTo(3)));
        assertThat(result.getRows().size(), is(equalTo(2)));
        assertThat(result.getRow(0).getName(), is(equalTo(mrkvicka().getName())));
        assertThat(result.getRow(1).getName(), is(equalTo(gates().getName())));

        userFilter = new UserFilter();
        userFilter.setPaginationData(new PaginationData(2, 2, "name", OrderMode.DESCENDING));

        result = userRepository.findByFilter(userFilter);
        assertThat(result.getNumberOfRows(), is(equalTo(3)));
        assertThat(result.getRows().size(), is(equalTo(1)));
        assertThat(result.getRow(0).getName(), is(equalTo(admin().getName())));
    }

    @Test
    public void findUserByFilterFilteringByName() {
        loadDataForFindByFilter();

        final UserFilter userFilter = new UserFilter();
        userFilter.setName("m");
        userFilter.setPaginationData(new PaginationData(0, 2, "name", OrderMode.ASCENDING));

        final PaginatedData<User> result = userRepository.findByFilter(userFilter);
        assertThat(result.getNumberOfRows(), is(equalTo(2)));
        assertThat(result.getRows().size(), is(equalTo(2)));
        assertThat(result.getRow(0).getName(), is(equalTo(admin().getName())));
        assertThat(result.getRow(1).getName(), is(equalTo(mrkvicka().getName())));
    }

    @Test
    public void findByFilterFilteringByNameAndType() {
        loadDataForFindByFilter();

        final UserFilter userFilter = new UserFilter();
        userFilter.setName("m");
        userFilter.setUserType(UserType.TEACHER);
        userFilter.setPaginationData(new PaginationData(0, 2, "name", OrderMode.ASCENDING));

        final PaginatedData<User> result = userRepository.findByFilter(userFilter);
        assertThat(result.getNumberOfRows(), is(equalTo(1)));
        assertThat(result.getRows().size(), is(equalTo(1)));
        assertThat(result.getRow(0).getName(), is(equalTo(admin().getName())));
    }

    //-------------------------------helper methods-------------------------------------

    private void loadDataForFindByFilter() {
        dbCommandExecutor.executeCommand(() -> {
            allStudents().forEach(user -> userRepository.add(normalizeDependencies(user, em)));
            //allTeachers().forEach(userRepository::add);
            return null;
        });
    }

    private void loadSchoolClasses() {
        dbCommandExecutor.executeCommand(() -> {
            allSchools().forEach(em::persist);
            allTeachers().forEach(em::persist);
            allSchoolClasses().forEach(schoolClass -> em.persist(normalizeDependencies(schoolClass, em)));
            return null;
        });
    }

    private void assertUser(final User actualUser, final User expectedUser, final UserType expectedUserType) {
        assertThat(actualUser.getName(), is(equalTo(expectedUser.getName())));
        assertThat(actualUser.getEmail(), is(equalTo(expectedUser.getEmail())));
        assertThat(actualUser.getRoles().toArray(), is(equalTo(expectedUser.getRoles().toArray())));
        assertThat(actualUser.getCreatedAt(), is(notNullValue()));
        assertThat(actualUser.getPassword(), is(expectedUser.getPassword()));
        assertThat(actualUser.getUserType(), is(equalTo(expectedUserType)));
        assertThat(actualUser.getSchoolClass(), is(equalTo(expectedUser.getSchoolClass())));
    }
}

