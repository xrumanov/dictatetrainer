package cz.muni.fi.dictatetrainer.schoolclass.services.impl;

import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.dictate.exception.DictateNotFoundException;
import cz.muni.fi.dictatetrainer.school.exception.SchoolNotFoundException;
import cz.muni.fi.dictatetrainer.school.services.SchoolServices;
import cz.muni.fi.dictatetrainer.schoolclass.exception.SchoolClassNotFoundException;
import cz.muni.fi.dictatetrainer.schoolclass.model.SchoolClass;
import cz.muni.fi.dictatetrainer.schoolclass.model.filter.SchoolClassFilter;
import cz.muni.fi.dictatetrainer.schoolclass.repository.SchoolClassRepository;
import cz.muni.fi.dictatetrainer.schoolclass.services.SchoolClassServices;
import cz.muni.fi.dictatetrainer.user.exception.UserNotFoundException;
import cz.muni.fi.dictatetrainer.user.model.User.Roles;
import cz.muni.fi.dictatetrainer.user.services.UserServices;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ejb.SessionContext;
import javax.validation.Validation;
import javax.validation.Validator;
import java.security.Principal;
import java.util.Arrays;

import static cz.muni.fi.dictatetrainer.commontests.schoolclass.SchoolClassesArgumentMatcher.schoolClassEq;
import static cz.muni.fi.dictatetrainer.commontests.schoolclass.SchoolClassesForTestRepository.schoolClass1;
import static cz.muni.fi.dictatetrainer.commontests.schoolclass.SchoolClassesForTestRepository.schoolClassWithId;
import static cz.muni.fi.dictatetrainer.commontests.user.UsersForTestRepository.admin;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class SchoolClassesServicesUnitTest {

    private Validator validator;
    private SchoolClassServices schoolClassServices;

    @Mock
    private SchoolClassRepository schoolClassRepository;

    @Mock
    private UserServices userServices;

    @Mock
    private SchoolServices schoolServices;

    @Mock
    private SessionContext sessionContext;

    private static final String LOGGED_EMAIL = "anyemail@domain.com";

    @Before
    public void initTestCase() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        MockitoAnnotations.initMocks(this);

        schoolClassServices = new SchoolClassServicesImpl();

        ((SchoolClassServicesImpl) schoolClassServices).validator = validator;
        ((SchoolClassServicesImpl) schoolClassServices).schoolClassRepository = schoolClassRepository;
        ((SchoolClassServicesImpl) schoolClassServices).userServices = userServices;
        ((SchoolClassServicesImpl) schoolClassServices).schoolServices = schoolServices;
        ((SchoolClassServicesImpl) schoolClassServices).sessionContext = sessionContext;

        setUpLoggedEmail(LOGGED_EMAIL, Roles.ADMINISTRATOR);
    }

    @Test(expected = UserNotFoundException.class)
    public void addSchoolClassWithNonexistentStudent() throws Exception {
        when(userServices.findById(anyLong())).thenThrow(new UserNotFoundException());

        schoolClassServices.add(schoolClass1());
    }

    @Test(expected = SchoolNotFoundException.class)
    public void addSchoolClassWithNonexistentSchool() {
        when(userServices.findById(anyLong())).thenReturn(admin());
        when(schoolServices.findById(anyLong())).thenThrow(new SchoolNotFoundException());

        schoolClassServices.add(schoolClass1());
    }

    @Test
    public void addSchoolClassWithNullSchool() {
        when(userServices.findById(anyLong())).thenReturn(admin());

        final SchoolClass schoolClass = schoolClass1();
        schoolClass.setSchool(null);

        addSchoolClassWithInvalidField(schoolClass, "school");
    }


    @Test
    public void addSchoolClassWithNullTeacher() {
        when(userServices.findById(anyLong())).thenReturn(admin());

        final SchoolClass schoolClass = schoolClass1();
        schoolClass.setTeacher(null);

        addSchoolClassWithInvalidField(schoolClass, "teacher");
    }

    @Test
    public void addValidSchoolClass() {
        when(userServices.findById(anyLong())).thenReturn(admin());
        when(schoolServices.findById(anyLong())).thenReturn(schoolClass1().getSchool());
        when(schoolClassRepository.add(schoolClassEq(schoolClass1()))).thenReturn(schoolClassWithId(schoolClass1(), 1L));

        final SchoolClass schoolClassAdded = schoolClassServices.add(schoolClass1());

        assertThat(schoolClassAdded.getId(), is(notNullValue()));
    }

    @Test(expected = SchoolClassNotFoundException.class)
    public void findSchoolClassByIdNotFound() {
        when(schoolClassRepository.findById(anyLong())).thenReturn(null);

        schoolClassServices.findById(1L);
    }

    @Test
    public void findSchoolClassById() {
        when(schoolClassRepository.findById(anyLong())).thenReturn(schoolClassWithId(schoolClass1(), 1L));

        final SchoolClass schoolClass = schoolClassServices.findById(1L);
        assertThat(schoolClass, is(notNullValue()));
    }

    @Test
    public void findByFilter() {
        final PaginatedData<SchoolClass> schoolClasss = new PaginatedData<SchoolClass>(1,
                Arrays.asList(schoolClass1()));
        when(schoolClassRepository.findByFilter((SchoolClassFilter) anyObject())).thenReturn(schoolClasss);

        final PaginatedData<SchoolClass> schoolClasssReturned = schoolClassServices.findByFilter(new SchoolClassFilter());
        assertThat(schoolClasssReturned.getNumberOfRows(), is(equalTo(1)));
        assertThat(schoolClasssReturned.getRows().size(), is(equalTo(1)));
    }

    private void addSchoolClassWithInvalidField(final SchoolClass schoolClass, final String invalidField) {
        try {
            schoolClassServices.add(schoolClass);
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo(invalidField)));
        }
    }

    private void setUpLoggedEmail(final String email, final Roles userRole) {
        reset(sessionContext);

        final Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(email);

        when(sessionContext.getCallerPrincipal()).thenReturn(principal);
        when(sessionContext.isCallerInRole(Roles.TEACHER.name())).thenReturn(userRole == Roles.TEACHER);
        when(sessionContext.isCallerInRole(Roles.STUDENT.name())).thenReturn(userRole == Roles.STUDENT);
    }
}
