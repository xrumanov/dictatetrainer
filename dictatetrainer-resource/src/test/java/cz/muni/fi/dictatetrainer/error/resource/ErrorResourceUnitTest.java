package cz.muni.fi.dictatetrainer.error.resource;

import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.model.HttpCode;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.utils.DateUtils;
import cz.muni.fi.dictatetrainer.commontests.utils.ResourceDefinitions;
import cz.muni.fi.dictatetrainer.dictate.exception.DictateNotFoundException;
import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.error.exception.ErrorNotFoundException;
import cz.muni.fi.dictatetrainer.error.model.Error;
import cz.muni.fi.dictatetrainer.error.model.filter.ErrorFilter;
import cz.muni.fi.dictatetrainer.error.services.ErrorServices;
import cz.muni.fi.dictatetrainer.trial.exception.TrialNotFoundException;
import cz.muni.fi.dictatetrainer.trial.model.Trial;
import cz.muni.fi.dictatetrainer.trial.resource.TrialJsonConverter;
import cz.muni.fi.dictatetrainer.user.exception.UserNotFoundException;
import cz.muni.fi.dictatetrainer.user.model.Student;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.util.Arrays;
import java.util.List;

import static cz.muni.fi.dictatetrainer.commontests.error.ErrorArgumentMatcher.errorEq;
import static cz.muni.fi.dictatetrainer.commontests.error.ErrorsForTestRepository.*;
import static cz.muni.fi.dictatetrainer.commontests.utils.FileTestNameUtils.getPathFileRequest;
import static cz.muni.fi.dictatetrainer.commontests.utils.FileTestNameUtils.getPathFileResponse;
import static cz.muni.fi.dictatetrainer.commontests.utils.JsonTestUtils.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ErrorResourceUnitTest {

    private ErrorResource errorResource;

    @Mock
    private ErrorServices errorServices;

    @Mock
    private UriInfo uriInfo;

    private static final String PATH_RESOURCE = ResourceDefinitions.ERROR.getResourceName();

    @Before
    public void initTestCase() {
        MockitoAnnotations.initMocks(this);

        errorResource = new ErrorResource();

        final ErrorJsonConverter errorJsonConverter = new ErrorJsonConverter();
        errorJsonConverter.trialJsonConverter = new TrialJsonConverter();

        errorResource.errorServices = errorServices;
        errorResource.uriInfo = uriInfo;
        errorResource.errorJsonConverter = errorJsonConverter;
    }

    @Test
    public void addValidError() throws Exception {
        final Error expectedError = errorManie();
        Student student = new Student();
        student.setId(1L);

        expectedError.setDictate(new Dictate(1L));
        expectedError.setStudent(student);
        expectedError.setTrial(new Trial(1L));
        when(errorServices.add(errorEq(expectedError))).thenReturn(errorWithId(errorManie(), 1L));

        final Response response = errorResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "sampleError.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.CREATED.getCode())));
        assertJsonMatchesExpectedJson(response.getEntity().toString(), "{\"id\": 1}");
    }

    @Test
    public void addErrorWithNullDescription() throws Exception {
        addErrorWithValidationError(
                new FieldNotValidException("errorDescription", "may not be null"), "sampleError.json",
                "errorNullDescription.json");
    }

        @Test
        public void addErrorWithNonexistentDictate() throws Exception {
            addErrorWithValidationError(new DictateNotFoundException(), "sampleError.json",
                    "errorNonexistentDictate.json");
        }

    @Test
    public void addErrorWithNonexistentStudent() throws Exception {
        addErrorWithValidationError(new UserNotFoundException(), "sampleError.json",
                "errorNonexistentStudent.json");
    }

    @Test
    public void addErrorWithNonexistentTrial() throws Exception {
        addErrorWithValidationError(new TrialNotFoundException(), "sampleError.json",
                "errorNonexistentTrial.json");
    }

        @Test
        public void findError() throws ErrorNotFoundException {
            final Error error = errorWithId(errorManie(), 1L);

            error.getDictate().setId(1L);
            error.getStudent().setId(2L);
            error.getTrial().setId(1L);
            error.getTrial().getStudent().setId(2L);
            error.getTrial().getDictate().setId(1L);
            error.getTrial().setPerformed(DateUtils.getAsDateTime("2015-11-04T10:10:34Z"));

            when(errorServices.findById(1L)).thenReturn(error);

            final Response response = errorResource.findById(1L);
            assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
            assertJsonResponseWithFile(response, "sampleErrorFound.json");
        }

        @Test
        public void findErrorNotFound() throws ErrorNotFoundException {
            when(errorServices.findById(1L)).thenThrow(new ErrorNotFoundException());

            final Response response = errorResource.findById(1L);
            assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
        }

        @SuppressWarnings("unchecked")
        @Test
        public void findByErrorNoFilter() {
            final List<Error> errors = Arrays.asList(errorWithId(errorManie(), 1L), errorWithId(errorPredlozky(), 2L));
            Long currentDictateId = 2L;
            Long currentTrialId = 1L;


            for (final Error error : errors) {
                error.getDictate().setId(currentDictateId);
                error.getStudent().setId(2L);
                error.getTrial().setId(currentTrialId);
                error.getTrial().getDictate().setId(currentDictateId);
                error.getTrial().getStudent().setId(2L);
                error.getTrial().setPerformed(DateUtils.getAsDateTime("2015-11-04T10:10:34Z"));
                currentDictateId--;
                currentTrialId = currentTrialId + 2;
            }

            final MultivaluedMap<String, String> multiMap = mock(MultivaluedMap.class);
            when(uriInfo.getQueryParameters()).thenReturn(multiMap);

            when(errorServices.findByFilter((ErrorFilter) anyObject())).thenReturn(
                    new PaginatedData<Error>(errors.size(), errors));

            final Response response = errorResource.findByFilter();
            assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
            assertJsonResponseWithFile(response, "errorsAllInOnePage.json");
        }

    private void addErrorWithValidationError(final Exception exceptionToBeThrown, final String requestFileName,
                                             final String responseFileName) throws Exception {
        when(errorServices.add((Error) anyObject())).thenThrow(exceptionToBeThrown);

        final Response response = errorResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE, requestFileName)));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, responseFileName);
    }

    private void assertJsonResponseWithFile(final Response response, final String fileName) {
        assertJsonMatchesFileContent(response.getEntity().toString(), getPathFileResponse(PATH_RESOURCE, fileName));
    }

}
