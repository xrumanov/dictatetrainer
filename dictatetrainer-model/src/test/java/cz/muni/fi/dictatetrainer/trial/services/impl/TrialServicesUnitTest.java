package cz.muni.fi.dictatetrainer.trial.services.impl;

import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.dictate.exception.DictateNotFoundException;
import cz.muni.fi.dictatetrainer.dictate.services.DictateServices;
import cz.muni.fi.dictatetrainer.trial.exception.TrialNotFoundException;
import cz.muni.fi.dictatetrainer.trial.model.Trial;
import cz.muni.fi.dictatetrainer.trial.model.filter.TrialFilter;
import cz.muni.fi.dictatetrainer.trial.repository.TrialRepository;
import cz.muni.fi.dictatetrainer.trial.services.TrialServices;
import cz.muni.fi.dictatetrainer.user.exception.UserNotFoundException;
import cz.muni.fi.dictatetrainer.user.model.Student;
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

import static cz.muni.fi.dictatetrainer.commontests.dictate.DictatesForTestRepository.*;
import static cz.muni.fi.dictatetrainer.commontests.trial.TrialArgumentMatcher.trialEq;
import static cz.muni.fi.dictatetrainer.commontests.trial.TrialsForTestRepository.*;
import static cz.muni.fi.dictatetrainer.commontests.user.UsersForTestRepository.gates;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class TrialServicesUnitTest {

    private Validator validator;
    private TrialServices trialServices;

    @Mock
    private TrialRepository trialRepository;

    @Mock
    private UserServices userServices;

    @Mock
    private DictateServices dictateServices;

    @Mock
    private SessionContext sessionContext;

    private static final String LOGGED_EMAIL = "anyemail@domain.com";

    @Before
    public void initTestCase() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        MockitoAnnotations.initMocks(this);

        trialServices = new TrialServicesImpl();

        ((TrialServicesImpl) trialServices).validator = validator;
        ((TrialServicesImpl) trialServices).trialRepository = trialRepository;
        ((TrialServicesImpl) trialServices).userServices = userServices;
        ((TrialServicesImpl) trialServices).dictateServices = dictateServices;
        ((TrialServicesImpl) trialServices).sessionContext = sessionContext;

        setUpLoggedEmail(LOGGED_EMAIL, Roles.ADMINISTRATOR);
    }

    @Test(expected = UserNotFoundException.class)
    public void addTrialWithNonexistentStudent() throws Exception {
        when(userServices.findByEmail(LOGGED_EMAIL)).thenThrow(new UserNotFoundException());

        trialServices.add(trialPerformed1());
    }

    @Test(expected = DictateNotFoundException.class)
    public void addTrialWithNonexistentDictate() {
        when(userServices.findByEmail(LOGGED_EMAIL)).thenReturn(gates());
        when(dictateServices.findById(anyLong())).thenThrow(new DictateNotFoundException());

        trialServices.add(trialPerformed1());
    }

    @Test
    public void addTrialWithNullDictate() throws Exception {
        when(userServices.findByEmail(LOGGED_EMAIL)).thenReturn(gates());

        final Trial trial = trialPerformed1();
        trial.setDictate(null);

        addTrialWithInvalidField(trial, "dictate");
    }
//
//    @Test
//    public void addValidTrial() {
//        when(userServices.findByEmail(LOGGED_EMAIL)).thenReturn(gates());
//        when(dictateServices.findById(1L)).thenReturn(dictateWithId(interpunkcia(), 1L));
//        when(trialRepository.add(trialEq(trialPerformed1()))).thenReturn(trialWithId(trialPerformed1(), 1L));
//
//        final Trial trial = new Trial();
//        trial.setTrialText("Nechce sa mi to pisat");
//        trial.setDictate(dictateServices.findById(1L));
//        trial.setStudent((Student) userServices.findByEmail(LOGGED_EMAIL));
//        final Long id = trialServices.add(trial).getId();
//
//        assertThat(id, is(notNullValue()));
//    }

    @Test(expected = TrialNotFoundException.class)
    public void findTrialByIdNotFound() {
        when(trialRepository.findById(1L)).thenReturn(null);

        trialServices.findById(1L);
    }

    @Test
    public void findTrialById() {
        when(trialRepository.findById(1L)).thenReturn(trialWithId(trialPerformed2(), 1L));

        final Trial trial = trialServices.findById(1L);
        assertThat(trial, is(notNullValue()));
    }

    @Test
    public void findByFilter() {
        final PaginatedData<Trial> trials = new PaginatedData<Trial>(2,
                Arrays.asList(trialPerformed1(), trialPerformed2()));
        when(trialRepository.findByFilter((TrialFilter) anyObject())).thenReturn(trials);

        final PaginatedData<Trial> trialsReturned = trialServices.findByFilter(new TrialFilter());
        assertThat(trialsReturned.getNumberOfRows(), is(equalTo(2)));
        assertThat(trialsReturned.getRows().size(), is(equalTo(2)));
    }

    private void addTrialWithInvalidField(final Trial trial, final String invalidField) {
        try {
            trialServices.add(trial);
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

