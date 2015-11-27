package cz.muni.fi.dictatetrainer.error.services.impl;

import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.dictate.exception.DictateNotFoundException;
import cz.muni.fi.dictatetrainer.dictate.services.DictateServices;
import cz.muni.fi.dictatetrainer.error.exception.ErrorNotFoundException;
import cz.muni.fi.dictatetrainer.error.model.Error;
import cz.muni.fi.dictatetrainer.error.model.filter.ErrorFilter;
import cz.muni.fi.dictatetrainer.error.repository.ErrorRepository;
import cz.muni.fi.dictatetrainer.error.services.ErrorServices;
import cz.muni.fi.dictatetrainer.trial.exception.TrialNotFoundException;
import cz.muni.fi.dictatetrainer.trial.services.TrialServices;
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

import static cz.muni.fi.dictatetrainer.commontests.error.ErrorArgumentMatcher.errorEq;
import static cz.muni.fi.dictatetrainer.commontests.error.ErrorsForTestRepository.errorPredlozky;
import static cz.muni.fi.dictatetrainer.commontests.error.ErrorsForTestRepository.errorWithId;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ErrorServicesUnitTest {

    private static Validator validator;
    private ErrorServices errorServices;

    @Mock
    private ErrorRepository errorRepository;

    @Mock
    private DictateServices dictateServices;

    @Mock
    private UserServices userServices;

    @Mock
    private TrialServices trialServices;

    @BeforeClass
    public static void initTestClass() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Before
    public void initTestCase() {
        MockitoAnnotations.initMocks(this);

        errorServices = new ErrorServicesImpl();

        ((ErrorServicesImpl) errorServices).errorRepository = errorRepository;
        ((ErrorServicesImpl) errorServices).validator = validator;
        ((ErrorServicesImpl) errorServices).userServices = userServices;
        ((ErrorServicesImpl) errorServices).dictateServices = dictateServices;
        ((ErrorServicesImpl) errorServices).trialServices = trialServices;
    }

    @Test
    public void addErrorWithNullErrorPriority() {
        final Error error = errorPredlozky();
        error.setErrorPriority(null);
        addErrorWithInvalidField(error, "errorPriority");
    }


    @Test
    public void addErrorWithNoDictate() {
        final Error error = errorPredlozky();
        error.setDictate(null);
        addErrorWithInvalidField(error, "dictate");
    }

    @Test
    public void addErrorWithNoStudent() {
        final Error error = errorPredlozky();
        error.setStudent(null);
    }

    @Test
    public void addErrorWithNoTrial() {
        final Error error = errorPredlozky();
        error.setTrial(null);
    }


    @Test(expected = DictateNotFoundException.class)
    public void addErrorWithNonexistentDictate() {
        when(dictateServices.findById(1L)).thenThrow(new DictateNotFoundException());

        final Error error = errorPredlozky();
        error.getDictate().setId(1L);

        errorServices.add(error);
    }

    @Test(expected = UserNotFoundException.class)
    public void addErrorWithNonexistentStudent() throws Exception {
        when(dictateServices.findById(anyLong())).thenReturn(errorPredlozky().getDictate());
        when(trialServices.findById(anyLong())).thenReturn(errorPredlozky().getTrial());
        when(userServices.findById(1L)).thenThrow(new UserNotFoundException());

        final Error error = errorPredlozky();
        error.getStudent().setId(1L);

        errorServices.add(error);
    }

    @Test(expected = TrialNotFoundException.class)
    public void addErrorWithNonexistentTrial() throws Exception {
        when(dictateServices.findById(anyLong())).thenReturn(errorPredlozky().getDictate());
        when(userServices.findById(anyLong())).thenReturn(errorPredlozky().getStudent());
        when(trialServices.findById(1L)).thenThrow(new TrialNotFoundException());

        final Error error = errorPredlozky();
        error.getTrial().setId(1L);

        errorServices.add(error);
    }

    @Test
    public void addValidError() throws Exception {
        when(dictateServices.findById(anyLong())).thenReturn(errorPredlozky().getDictate());
        when(userServices.findById(anyLong())).thenReturn(errorPredlozky().getStudent());
        when(trialServices.findById(anyLong())).thenReturn(errorPredlozky().getTrial());
        when(errorRepository.add(errorEq(errorPredlozky()))).thenReturn(errorWithId(errorPredlozky(), 1L));

        final Error errorAdded = errorServices.add(errorPredlozky());
        assertThat(errorAdded.getId(), equalTo(1L));
    }


    @Test(expected = ErrorNotFoundException.class)
    public void updateErrorNotFound() throws Exception {
        when(errorRepository.existsById(1L)).thenReturn(false);

        errorServices.update(errorWithId(errorPredlozky(), 1L));
    }

    @Test(expected = DictateNotFoundException.class)
    public void updateErrorWithNonexistentDictate() throws Exception {
        when(errorRepository.existsById(1L)).thenReturn(true);
        when(dictateServices.findById(1L)).thenThrow(new DictateNotFoundException());

        final Error error = errorWithId(errorPredlozky(), 1L);
        error.getDictate().setId(1L);

        errorServices.update(error);
    }

    @Test(expected = UserNotFoundException.class)
    public void updateErrorWithNonexistentStudent() throws Exception {
        when(errorRepository.existsById(1L)).thenReturn(true);
        when(dictateServices.findById(anyLong())).thenReturn(errorPredlozky().getDictate());
        when(trialServices.findById(anyLong())).thenReturn(errorPredlozky().getTrial());
        when(userServices.findById(1L)).thenThrow(new UserNotFoundException());

        final Error error = errorWithId(errorPredlozky(), 1L);
        error.getStudent().setId(1L);

        errorServices.update(error);
    }

    @Test(expected = TrialNotFoundException.class)
    public void updateErrorWithNonexistentTrial() throws Exception {
        when(errorRepository.existsById(1L)).thenReturn(true);
        when(dictateServices.findById(anyLong())).thenReturn(errorPredlozky().getDictate());
        when(userServices.findById(anyLong())).thenReturn(errorPredlozky().getStudent());
        when(trialServices.findById(1L)).thenThrow(new TrialNotFoundException());

        final Error error = errorWithId(errorPredlozky(), 1L);
        error.getTrial().setId(1L);

        errorServices.update(error);
    }

    @Test
    public void updateValidError() throws Exception {
        final Error errorToUpdate = errorWithId(errorPredlozky(), 1L);
        when(dictateServices.findById(anyLong())).thenReturn(errorPredlozky().getDictate());
        when(userServices.findById(anyLong())).thenReturn(errorPredlozky().getStudent());
        when(trialServices.findById(anyLong())).thenReturn(errorPredlozky().getTrial());
        when(errorRepository.existsById(1L)).thenReturn(true);

        errorServices.update(errorToUpdate);
        verify(errorRepository).update(errorEq(errorToUpdate));
    }

    @Test(expected = ErrorNotFoundException.class)
    public void findErrorByIdNotFound() throws ErrorNotFoundException {
        when(errorRepository.findById(1L)).thenReturn(null);

        errorServices.findById(1L);
    }

    @Test
    public void findErrorByFilter() {
        final PaginatedData<Error> errors = new PaginatedData<Error>(1, Arrays.asList(errorWithId(errorPredlozky(), 1L)));
        when(errorRepository.findByFilter((ErrorFilter) anyObject())).thenReturn(errors);

        final PaginatedData<Error> errorsReturned = errorServices.findByFilter(new ErrorFilter());
        assertThat(errorsReturned.getNumberOfRows(), is(equalTo(1)));
        assertThat(errorsReturned.getRow(0).getId(), is(equalTo(1L)));
    }

    @Test
    public void findErrorById() throws ErrorNotFoundException {
        when(errorRepository.findById(1L)).thenReturn(errorWithId(errorPredlozky(), 1L));

        final Error error = errorServices.findById(1L);
        assertThat(error, is(notNullValue()));
        assertThat(error.getCorrectWord(), is(equalTo(errorPredlozky().getCorrectWord())));
    }

    private void addErrorWithInvalidField(final Error error, final String expectedInvalidFieldName) {
        try {
            errorServices.add(error);
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo(expectedInvalidFieldName)));
        }
    }
}
