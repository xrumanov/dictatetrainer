package cz.muni.fi.dictatetrainer.dictate.services.impl;

import cz.muni.fi.dictatetrainer.category.exception.CategoryNotFoundException;
import cz.muni.fi.dictatetrainer.category.services.CategoryServices;
import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.dictate.exception.DictateNotFoundException;
import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.dictate.model.filter.DictateFilter;
import cz.muni.fi.dictatetrainer.dictate.repository.DictateRepository;
import cz.muni.fi.dictatetrainer.dictate.services.DictateServices;
import cz.muni.fi.dictatetrainer.user.exception.UserNotFoundException;
import cz.muni.fi.dictatetrainer.user.services.UserServices;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;

import static cz.muni.fi.dictatetrainer.commontests.dictate.DictateArgumentMatcher.dictateEq;
import static cz.muni.fi.dictatetrainer.commontests.dictate.DictatesForTestRepository.*;
import static cz.muni.fi.dictatetrainer.commontests.user.UsersForTestRepository.admin;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DictateServicesUnitTest {

    private static Validator validator;
    private DictateServices dictateServices;

    @Mock
    private DictateRepository dictateRepository;

    @Mock
    private CategoryServices categoryServices;

    @Mock
    private UserServices userServices;

    @BeforeClass
    public static void initTestClass() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Before
    public void initTestCase() {
        MockitoAnnotations.initMocks(this);

        dictateServices = new DictateServicesImpl();

        ((DictateServicesImpl) dictateServices).dictateRepository = dictateRepository;
        ((DictateServicesImpl) dictateServices).validator = validator;
        ((DictateServicesImpl) dictateServices).categoryServices = categoryServices;
        ((DictateServicesImpl) dictateServices).userServices = userServices;
    }

    @Test
    public void addDictateWithNullTitle() {
        final Dictate dictate = vybraneSlova1();
        dictate.setName(null);
        addDictateWithInvalidField(dictate, "name");
    }

    @Test
    public void addDictateWithNullCategory() {
        final Dictate dictate = vybraneSlova1();
        dictate.setCategory(null);
        addDictateWithInvalidField(dictate, "category");
    }

    @Test
    public void addDictateWithNoUploader() {
        final Dictate dictate = vybraneSlova1();
        dictate.setUploader(null);
    }

    @Test
    public void addDictateWithShortDescription() {
        final Dictate dictate = vybraneSlova1();
        dictate.setDescription("short");
        addDictateWithInvalidField(dictate, "description");
    }

    @Test(expected = CategoryNotFoundException.class)
    public void addDictateWithNonexistentCategory() {
        when(categoryServices.findById(1L)).thenThrow(new CategoryNotFoundException());

        final Dictate dictate = vybraneSlova1();
        dictate.getCategory().setId(1L);

        dictateServices.add(dictate);
    }

    @Test(expected = UserNotFoundException.class)
    public void addDictateWithNonexistentUploader() throws Exception {
        when(categoryServices.findById(anyLong())).thenReturn(interpunkcia().getCategory());
        when(userServices.findById(1L)).thenThrow(new UserNotFoundException());

        final Dictate dictate = interpunkcia();
        dictate.getUploader().setId(1L);

        dictateServices.add(dictate);
    }

    @Test
    public void addValidDictate() throws Exception {
        when(categoryServices.findById(anyLong())).thenReturn(vybraneSlova1().getCategory());
        when(userServices.findById(anyLong())).thenReturn(admin());
        when(dictateRepository.add(dictateEq(vybraneSlova1()))).thenReturn(dictateWithId(vybraneSlova1(), 1L));

        final Dictate dictateAdded = dictateServices.add(vybraneSlova1());
        assertThat(dictateAdded.getId(), equalTo(1L));
    }

    @Test
    public void updateAuthorWithShortTitle() {
        final Dictate dictate = vybraneSlova1();
        dictate.setName("short");
        try {
            dictateServices.update(dictate);
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("name")));
        } catch (final Exception e) {
            fail("An Exception should not have been thrown");
        }
    }

    @Test(expected = DictateNotFoundException.class)
    public void updateDictateNotFound() throws Exception {
        when(dictateRepository.existsById(1L)).thenReturn(false);

        dictateServices.update(dictateWithId(vybraneSlova1(), 1L));
    }

    @Test(expected = CategoryNotFoundException.class)
    public void updateDictateWithInexistentCategory() throws Exception {
        when(dictateRepository.existsById(1L)).thenReturn(true);
        when(categoryServices.findById(1L)).thenThrow(new CategoryNotFoundException());

        final Dictate dictate = dictateWithId(vybraneSlova1(), 1L);
        dictate.getCategory().setId(1L);

        dictateServices.update(dictate);
    }

    @Test(expected = UserNotFoundException.class)
    public void updateDictateWithNonexistentUploader() throws Exception {
        when(dictateRepository.existsById(1L)).thenReturn(true);
        when(categoryServices.findById(anyLong())).thenReturn(vybraneSlova1().getCategory());
        when(userServices.findById(1L)).thenThrow(new UserNotFoundException());

        final Dictate dictate = dictateWithId(interpunkcia(), 1L);
        dictate.getUploader().setId(1L);

        dictateServices.update(dictate);
    }

    @Test
    public void updateValidDictate() throws Exception {
        final Dictate dictateToUpdate = dictateWithId(vybraneSlova1(), 1L);
        when(categoryServices.findById(anyLong())).thenReturn(vybraneSlova1().getCategory());
        when(userServices.findById(anyLong())).thenReturn(admin());
        when(dictateRepository.existsById(1L)).thenReturn(true);

        dictateServices.update(dictateToUpdate);
        verify(dictateRepository).update(dictateEq(dictateToUpdate));
    }

    @Test(expected = DictateNotFoundException.class)
    public void findDictateByIdNotFound() throws DictateNotFoundException {
        when(dictateRepository.findById(1L)).thenReturn(null);

        dictateServices.findById(1L);
    }

    @Test
    public void findDictateByFilter() {
        final PaginatedData<Dictate> dictates = new PaginatedData<Dictate>(1, Arrays.asList(dictateWithId(vybraneSlova1(), 1L)));
        when(dictateRepository.findByFilter((DictateFilter) anyObject())).thenReturn(dictates);

        final PaginatedData<Dictate> dictatesReturned = dictateServices.findByFilter(new DictateFilter());
        assertThat(dictatesReturned.getNumberOfRows(), is(equalTo(1)));
        assertThat(dictatesReturned.getRow(0).getName(), is(equalTo(vybraneSlova1().getName())));
    }

    @Test
    public void findDictateById() throws DictateNotFoundException {
        when(dictateRepository.findById(1L)).thenReturn(dictateWithId(vybraneSlova1(), 1L));

        final Dictate dictate = dictateServices.findById(1L);
        assertThat(dictate, is(notNullValue()));
        assertThat(dictate.getName(), is(equalTo(vybraneSlova1().getName())));
    }

    private void addDictateWithInvalidField(final Dictate dictate, final String expectedInvalidFieldName) {
        try {
            dictateServices.add(dictate);
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo(expectedInvalidFieldName)));
        }
    }
}

