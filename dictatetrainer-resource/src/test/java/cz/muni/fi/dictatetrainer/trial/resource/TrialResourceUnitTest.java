package cz.muni.fi.dictatetrainer.trial.resource;

import static cz.muni.fi.dictatetrainer.commontests.trial.TrialArgumentMatcher.*;
import static cz.muni.fi.dictatetrainer.commontests.trial.TrialsForTestRepository.*;
import static cz.muni.fi.dictatetrainer.commontests.utils.FileTestNameUtils.*;
import static cz.muni.fi.dictatetrainer.commontests.utils.JsonTestUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import cz.muni.fi.dictatetrainer.common.utils.DateUtils;
import cz.muni.fi.dictatetrainer.dictate.exception.DictateNotFoundException;
import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.dictate.resource.DictateJsonConverter;
import cz.muni.fi.dictatetrainer.user.exception.UserNotFoundException;
import cz.muni.fi.dictatetrainer.user.model.Student;
import cz.muni.fi.dictatetrainer.user.model.Teacher;
import cz.muni.fi.dictatetrainer.user.model.User;
import cz.muni.fi.dictatetrainer.user.resource.UserJsonConverter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.muni.fi.dictatetrainer.trial.exception.TrialNotFoundException;
import cz.muni.fi.dictatetrainer.trial.model.Trial;
import cz.muni.fi.dictatetrainer.trial.model.filter.TrialFilter;
import cz.muni.fi.dictatetrainer.trial.services.TrialServices;
import cz.muni.fi.dictatetrainer.category.exception.CategoryNotFoundException;
import cz.muni.fi.dictatetrainer.category.model.Category;
import cz.muni.fi.dictatetrainer.category.resource.CategoryJsonConverter;
import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.model.HttpCode;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.commontests.utils.ResourceDefinitions;

public class TrialResourceUnitTest {

        private TrialResource trialResource;

        @Mock
        private TrialServices trialServices;

        @Mock
        private UriInfo uriInfo;

        private static final String PATH_RESOURCE = ResourceDefinitions.TRIAL.getResourceName();

        @Before
        public void initTestCase() {
            MockitoAnnotations.initMocks(this);

            trialResource = new TrialResource();

            final TrialJsonConverter trialJsonConverter = new TrialJsonConverter();

            trialResource.trialServices = trialServices;
            trialResource.uriInfo = uriInfo;
            trialResource.trialJsonConverter = trialJsonConverter;

        }

        @Test
        public void addValidTrial() throws Exception {
            final Trial expectedTrial = trialPerformed1();
            Student student = new Student();
            student.setId(1L);

            expectedTrial.setDictate(new Dictate(1L));
            expectedTrial.setStudent(student);
            when(trialServices.add(trialEq(expectedTrial))).thenReturn(trialWithId(trialPerformed1(), 1L));

            final Response response =
                    trialResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "trialMrkvickaSampleDictate.json")));
            assertThat(response.getStatus(), is(equalTo(HttpCode.CREATED.getCode())));
            assertJsonMatchesExpectedJson(response.getEntity().toString(), "{\"id\": 1}");
        }


        @Test
        public void addTrialWithNullTrialText() throws Exception {
            addTrialWithValidationError(new FieldNotValidException("trialText", "may not be null"), "trialMrkvickaSampleDictate.json",
                    "trialNullTrialText.json");
        }

        @Test
        public void addTrialWithNonexistentDictate() throws Exception {
            addTrialWithValidationError(new DictateNotFoundException(), "trialMrkvickaSampleDictate.json",
                    "trialNonexistentDictate.json");
        }

        @Test
        public void addTrialWithNonexistentStudent() throws Exception {
            addTrialWithValidationError(new UserNotFoundException(), "trialMrkvickaSampleDictate.json", "trialNonexistentStudent.json");
        }

        @Test
        public void findTrial() throws TrialNotFoundException {
            final Trial trial = trialWithId(trialPerformed1(), 1L);
            trial.getDictate().setId(1L);
            trial.getStudent().setId(1L);
            trial.setPerformed(DateUtils.getAsDateTime("2015-11-04T10:10:34Z"));

            when(trialServices.findById(1L)).thenReturn(trial);

            final Response response = trialResource.findById(1L);
            assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
            assertJsonResponseWithFile(response, "trialFound.json");
        }

        @Test
        public void findTrialNotFound() throws TrialNotFoundException {
            when(trialServices.findById(1L)).thenThrow(new TrialNotFoundException());

            final Response response = trialResource.findById(1L);
            assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
        }

        @SuppressWarnings("unchecked")
        @Test
        public void findByTrialNoFilter() {
            final List<Trial> trials = Arrays.asList(trialWithId(trialPerformed1(), 1L), trialWithId(trialPerformed2(), 2L));
            Long currentStudentId = 1L;
            Long currentDictateId = 1L;
            for (final Trial trial : trials) {
                trial.getDictate().setId(currentDictateId++);
                trial.getStudent().setId(currentStudentId++);
                trial.setPerformed(DateUtils.getAsDateTime("2015-11-04T10:10:34Z"));
            }

            final MultivaluedMap<String, String> multiMap = mock(MultivaluedMap.class);
            when(uriInfo.getQueryParameters()).thenReturn(multiMap);

            when(trialServices.findByFilter((TrialFilter) anyObject())).thenReturn(
                    new PaginatedData<Trial>(trials.size(), trials));

            final Response response = trialResource.findByFilter();
            assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
            assertJsonResponseWithFile(response, "trialsAllInOnePage.json");
        }

        private void addTrialWithValidationError(final Exception exceptionToBeThrown, final String requestFileName,
                                                   final String responseFileName) throws Exception {
            when(trialServices.add((Trial) anyObject())).thenThrow(exceptionToBeThrown);

            final Response response = trialResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE, requestFileName)));
            assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
            assertJsonResponseWithFile(response, responseFileName);
        }

        private void assertJsonResponseWithFile(final Response response, final String fileName) {
            assertJsonMatchesFileContent(response.getEntity().toString(), getPathFileResponse(PATH_RESOURCE, fileName));
        }
}
