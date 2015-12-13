package cz.muni.fi.dictatetrainer.school.services.impl;

import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.school.exception.SchoolExistentException;
import cz.muni.fi.dictatetrainer.school.exception.SchoolNotFoundException;
import cz.muni.fi.dictatetrainer.school.model.School;
import cz.muni.fi.dictatetrainer.school.repository.SchoolRepository;
import cz.muni.fi.dictatetrainer.school.services.SchoolServices;
import org.junit.Before;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cz.muni.fi.dictatetrainer.commontests.school.SchoolsForTestRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class SchoolServicesUnitTest {

    private SchoolServices schoolServices;
    private SchoolRepository schoolRepository;
    private Validator validator;

    @Before
    public void initTestCase() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        schoolRepository = mock(SchoolRepository.class);

        schoolServices = new SchoolServicesImpl();
        ((SchoolServicesImpl) schoolServices).validator = validator;
        ((SchoolServicesImpl) schoolServices).schoolRepository = schoolRepository;
    }

    @Test
    public void addSchoolWithNullName() {
        addSchoolWithInvalidName(null);
    }

    @Test
    public void addSchoolWithShortName() {
        addSchoolWithInvalidName("A");
    }

    @Test
    public void addSchoolWithLongName() {
        addSchoolWithInvalidName("This is a long name that will cause an exception to be thrown");
    }

    @Test(expected = SchoolExistentException.class)
    public void addSchoolWithExistentName() {
        when(schoolRepository.alreadyExists(school1())).thenReturn(true);

        schoolServices.add(school1());
    }

    @Test
    public void addValidSchool() {
        when(schoolRepository.alreadyExists(school1())).thenReturn(false);
        when(schoolRepository.add(school1())).thenReturn(schoolWithId(school1(), 1L));

        final School schoolAdded = schoolServices.add(school1());
        assertThat(schoolAdded.getId(), is(equalTo(1L)));
    }

    @Test
    public void updateWithNullName() {
        updateSchoolWithInvalidName(null);
    }

    @Test
    public void updateSchoolWithShortName() {
        updateSchoolWithInvalidName("A");
    }

    @Test
    public void updateSchoolWithLongName() {
        updateSchoolWithInvalidName("This is a long name that will cause an exception to be thrown");
    }

    @Test(expected = SchoolExistentException.class)
    public void updateSchoolWithExistentName() {
        when(schoolRepository.alreadyExists(schoolWithId(school1(), 1L))).thenReturn(true);

        schoolServices.update(schoolWithId(school1(), 1L));
    }

    @Test(expected = SchoolNotFoundException.class)
    public void updateSchoolNotFound() {
        when(schoolRepository.alreadyExists(schoolWithId(school1(), 1L))).thenReturn(false);
        when(schoolRepository.existsById(1L)).thenReturn(false);

        schoolServices.update(schoolWithId(school1(), 1L));
    }

    @Test
    public void updateValidSchool() {
        when(schoolRepository.alreadyExists(schoolWithId(school1(), 1L))).thenReturn(false);
        when(schoolRepository.existsById(1L)).thenReturn(true);

        schoolServices.update(schoolWithId(school1(), 1L));

        verify(schoolRepository).update(schoolWithId(school1(), 1L));
    }

    @Test
    public void findSchoolById() {
        when(schoolRepository.findById(1L)).thenReturn(schoolWithId(school1(), 1L));

        final School school = schoolServices.findById(1L);
        assertThat(school, is(notNullValue()));
        assertThat(school.getId(), is(equalTo(1L)));
        assertThat(school.getName(), is(equalTo(school1().getName())));
    }

    @Test(expected = SchoolNotFoundException.class)
    public void findSchoolByIdNotFound() {
        when(schoolRepository.findById(1L)).thenReturn(null);

        schoolServices.findById(1L);
    }

    @Test
    public void findAllNoCategories() {
        when(schoolRepository.findAll("name")).thenReturn(new ArrayList<>());

        final List<School> categories = schoolServices.findAll();
        assertThat(categories.isEmpty(), is(equalTo(true)));
    }

    @Test
    public void findAllCategories() {
        when(schoolRepository.findAll("name")).thenReturn(
                Arrays.asList(schoolWithId(school1(), 1L), schoolWithId(school2(), 2L), schoolWithId(school3(), 3L)));

        final List<School> categories = schoolServices.findAll();
        assertThat(categories.size(), is(equalTo(3)));
        assertThat(categories.get(0).getName(), is(equalTo(school1().getName())));
        assertThat(categories.get(1).getName(), is(equalTo(school2().getName())));
        assertThat(categories.get(2).getName(), is(equalTo(school3().getName())));
    }

    private void addSchoolWithInvalidName(final String name) {
        try {
            schoolServices.add(new School(name));
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("name")));
        }
    }

    private void updateSchoolWithInvalidName(final String name) {
        try {
            schoolServices.update(new School(name));
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("name")));
        }
    }

}
