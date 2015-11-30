package cz.muni.fi.dictatetrainer.dictate.resource;

import static cz.muni.fi.dictatetrainer.commontests.dictate.DictateArgumentMatcher.*;
import static cz.muni.fi.dictatetrainer.commontests.dictate.DictatesForTestRepository.*;
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
import cz.muni.fi.dictatetrainer.user.exception.UserNotFoundException;
import cz.muni.fi.dictatetrainer.user.model.Teacher;
import cz.muni.fi.dictatetrainer.user.model.User;
import cz.muni.fi.dictatetrainer.user.resource.UserJsonConverter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.muni.fi.dictatetrainer.dictate.exception.DictateNotFoundException;
import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.dictate.model.filter.DictateFilter;
import cz.muni.fi.dictatetrainer.dictate.services.DictateServices;
import cz.muni.fi.dictatetrainer.category.exception.CategoryNotFoundException;
import cz.muni.fi.dictatetrainer.category.model.Category;
import cz.muni.fi.dictatetrainer.category.resource.CategoryJsonConverter;
import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.model.HttpCode;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.commontests.utils.ResourceDefinitions;

public class DictateResourceUnitTest {

        private DictateResource dictateResource;

        @Mock
        private DictateServices dictateServices;

        @Mock
        private UriInfo uriInfo;

        private static final String PATH_RESOURCE = ResourceDefinitions.DICTATE.getResourceName();

        @Before
        public void initTestCase() {
            MockitoAnnotations.initMocks(this);

            dictateResource = new DictateResource();

            final DictateJsonConverter dictateJsonConverter = new DictateJsonConverter();
            dictateJsonConverter.categoryJsonConverter = new CategoryJsonConverter();
            dictateJsonConverter.userJsonConverter = new UserJsonConverter();

            dictateResource.dictateServices = dictateServices;
            dictateResource.uriInfo = uriInfo;
            dictateResource.dictateJsonConverter = dictateJsonConverter;
        }

        @Test
        public void addValidDictate() throws Exception {
            final Dictate expectedDictate = interpunkcia();
            Teacher uploader = new Teacher();
            uploader.setId(1L);

            expectedDictate.setCategory(new Category(1L));
            expectedDictate.setUploader(uploader);
            when(dictateServices.add(dictateEq(expectedDictate))).thenReturn(dictateWithId(interpunkcia(), 1L));

            final Response response = dictateResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "sampleDictate.json")));
            assertThat(response.getStatus(), is(equalTo(HttpCode.CREATED.getCode())));
            assertJsonMatchesExpectedJson(response.getEntity().toString(), "{\"id\": 1}");
        }

        @Test
        public void addDictateWithNullFilename() throws Exception {
            addDictateWithValidationError(new FieldNotValidException("filename", "may not be null"), "sampleDictate.json",
                    "dictateNullFilename.json");
        }

        @Test
        public void addDictateWithNonexistentCategory() throws Exception {
            addDictateWithValidationError(new CategoryNotFoundException(), "sampleDictate.json",
                    "dictateNonexistentCategory.json");
        }

        @Test
        public void addDictateWithNonexistentUploader() throws Exception {
            addDictateWithValidationError(new UserNotFoundException(), "sampleDictate.json", "dictateNonexistentUploader.json");
        }

        @Test
        public void updateValidDictate() throws Exception {
            final Response response = dictateResource.update(1L,
                    readJsonFile(getPathFileRequest(PATH_RESOURCE, "sampleDictate.json")));
            final Teacher uploader = new Teacher();
            uploader.setId(1L);

            assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
            assertThat(response.getEntity().toString(), is(equalTo("")));

            final Dictate expectedDictate = dictateWithId(interpunkcia(), 1L);
            expectedDictate.setCategory(new Category(1L));
            expectedDictate.setUploader(uploader);
            verify(dictateServices).update(dictateEq(expectedDictate));
        }

        @Test
        public void updateDictateWithNullFilename() throws Exception {
            updateDictateWithError(new FieldNotValidException("filename", "may not be null"), HttpCode.VALIDATION_ERROR,
                    "sampleDictate.json", "dictateNullFilename.json");
        }

        @Test
        public void updateDictateWithNonexistentCategory() throws Exception {
            updateDictateWithError(new CategoryNotFoundException(), HttpCode.VALIDATION_ERROR, "sampleDictate.json",
                    "dictateNonexistentCategory.json");
        }

        @Test
        public void updateDictateWithNonexistentUploader() throws Exception {
            updateDictateWithError(new UserNotFoundException(), HttpCode.VALIDATION_ERROR, "sampleDictate.json",
                    "dictateNonexistentUploader.json");
        }

        @Test
        public void updateDictateNotFound() throws Exception {
            updateDictateWithError(new DictateNotFoundException(), HttpCode.NOT_FOUND, "sampleDictate.json",
                    "dictateErrorNonExistentDictate.json");
        }

        @Test
        public void findDictate() throws DictateNotFoundException {
            final Dictate dictate = dictateWithId(interpunkcia(), 1L);
            dictate.getCategory().setId(1L);
            dictate.getUploader().setId(1L);
            dictate.getUploader().setCreatedAt(DateUtils.getAsDateTime("2015-11-03T22:35:42Z"));
            when(dictateServices.findById(1L)).thenReturn(dictate);

            final Response response = dictateResource.findById(1L);
            assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
            assertJsonResponseWithFile(response, "sampleDictateFound.json");
        }

        @Test
        public void findDictateNotFound() throws DictateNotFoundException {
            when(dictateServices.findById(1L)).thenThrow(new DictateNotFoundException());

            final Response response = dictateResource.findById(1L);
            assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
        }

        @SuppressWarnings("unchecked")
        @Test
        public void findByDictateNoFilter() {
            final List<Dictate> dictates = Arrays.asList(dictateWithId(interpunkcia(), 1L), dictateWithId(vybraneSlova1(), 2L));
            Long currentCategoryId = 1L;
            for (final Dictate dictate : dictates) {
                dictate.getCategory().setId(currentCategoryId++);
                dictate.getUploader().setId(1L);
                dictate.getUploader().setCreatedAt(DateUtils.getAsDateTime("2015-11-03T22:35:42Z"));
            }

            final MultivaluedMap<String, String> multiMap = mock(MultivaluedMap.class);
            when(uriInfo.getQueryParameters()).thenReturn(multiMap);

            when(dictateServices.findByFilter((DictateFilter) anyObject())).thenReturn(
                    new PaginatedData<Dictate>(dictates.size(), dictates));

            final Response response = dictateResource.findByFilter();
            assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
            assertJsonResponseWithFile(response, "dictatesAllInOnePage.json");
        }

        private void addDictateWithValidationError(final Exception exceptionToBeThrown, final String requestFileName,
                                                final String responseFileName) throws Exception {
            when(dictateServices.add((Dictate) anyObject())).thenThrow(exceptionToBeThrown);

            final Response response = dictateResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE, requestFileName)));
            assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
            assertJsonResponseWithFile(response, responseFileName);
        }

        private void updateDictateWithError(final Exception exceptionToBeThrown, final HttpCode expectedHttpCode,
                                         final String requestFileName,
                                         final String responseFileName) throws Exception {
            doThrow(exceptionToBeThrown).when(dictateServices).update(dictateWithId(interpunkcia(), 1L));

            final Response response = dictateResource.update(1L,
                    readJsonFile(getPathFileRequest(PATH_RESOURCE, requestFileName)));
            assertThat(response.getStatus(), is(equalTo(expectedHttpCode.getCode())));
            if (expectedHttpCode != HttpCode.NOT_FOUND) {
                assertJsonResponseWithFile(response, responseFileName);
            }
        }

        private void assertJsonResponseWithFile(final Response response, final String fileName) {
            assertJsonMatchesFileContent(response.getEntity().toString(), getPathFileResponse(PATH_RESOURCE, fileName));
        }
}
