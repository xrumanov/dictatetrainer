package cz.muni.fi.dictatetrainer.schoolclass.resource;

import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.model.HttpCode;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.utils.DateUtils;
import cz.muni.fi.dictatetrainer.commontests.utils.ResourceDefinitions;
import cz.muni.fi.dictatetrainer.school.exception.SchoolNotFoundException;
import cz.muni.fi.dictatetrainer.school.model.School;
import cz.muni.fi.dictatetrainer.school.resource.SchoolJsonConverter;
import cz.muni.fi.dictatetrainer.schoolclass.exception.SchoolClassNotFoundException;
import cz.muni.fi.dictatetrainer.schoolclass.model.SchoolClass;
import cz.muni.fi.dictatetrainer.schoolclass.model.filter.SchoolClassFilter;
import cz.muni.fi.dictatetrainer.schoolclass.services.SchoolClassServices;
import cz.muni.fi.dictatetrainer.user.exception.UserNotFoundException;
import cz.muni.fi.dictatetrainer.user.model.Teacher;
import cz.muni.fi.dictatetrainer.user.resource.UserJsonConverter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;
import java.util.List;

import static cz.muni.fi.dictatetrainer.commontests.schoolclass.SchoolClassesArgumentMatcher.schoolClassEq;
import static cz.muni.fi.dictatetrainer.commontests.schoolclass.SchoolClassesForTestRepository.*;
import static cz.muni.fi.dictatetrainer.commontests.utils.FileTestNameUtils.getPathFileRequest;
import static cz.muni.fi.dictatetrainer.commontests.utils.FileTestNameUtils.getPathFileResponse;
import static cz.muni.fi.dictatetrainer.commontests.utils.JsonTestUtils.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class SchoolClassResourceUnitTest {

    private SchoolClassResource schoolClassResource;

    @Mock
    private SchoolClassServices schoolClassServices;

    @Mock
    private UriInfo uriInfo;

    private static final String PATH_RESOURCE = ResourceDefinitions.SCHOOLCLASS.getResourceName();

    @Before
    public void initTestCase() {
        MockitoAnnotations.initMocks(this);

        schoolClassResource = new SchoolClassResource();

        final SchoolClassJsonConverter schoolClassJsonConverter = new SchoolClassJsonConverter();
        schoolClassJsonConverter.schoolJsonConverter = new SchoolJsonConverter();
        schoolClassJsonConverter.userJsonConverter = new UserJsonConverter();

        schoolClassResource.schoolClassServices = schoolClassServices;
        schoolClassResource.uriInfo = uriInfo;
        schoolClassResource.schoolClassJsonConverter = schoolClassJsonConverter;
    }

    @Test
    public void addValidSchoolClass() throws Exception {
        final SchoolClass expectedSchoolClass = schoolClass1();
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        expectedSchoolClass.setSchool(new School(1L));
        expectedSchoolClass.setTeacher(teacher);
        when(schoolClassServices.add(schoolClassEq(expectedSchoolClass))).thenReturn(schoolClassWithId(schoolClass1(), 1L));

        final Response response = schoolClassResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "sampleSchoolClass.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.CREATED.getCode())));
        assertJsonMatchesExpectedJson(response.getEntity().toString(), "{\"id\": 1}");
    }

    @Test
    public void addSchoolClassWithNullName() throws Exception {
        addSchoolClassWithValidationError(new FieldNotValidException("name", "may not be null"), "sampleSchoolClass.json",
                "schoolClassNullName.json");
    }

    @Test
    public void addSchoolClassWithNonexistentSchool() throws Exception {
        addSchoolClassWithValidationError(new SchoolNotFoundException(), "sampleSchoolClass.json",
                "schoolClassNonexistentSchool.json");
    }

    @Test
    public void addSchoolClassWithNonexistentTeacher() throws Exception {
        addSchoolClassWithValidationError(new UserNotFoundException(), "sampleSchoolClass.json", "schoolClassNonexistentTeacher.json");
    }

    @Test
    public void updateValidSchoolClass() throws Exception {
        final Response response = schoolClassResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "sampleSchoolClass.json")));
        final Teacher teacher = new Teacher();
        teacher.setId(1L);

        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertThat(response.getEntity().toString(), is(equalTo("")));

        final SchoolClass expectedSchoolClass = schoolClassWithId(schoolClass1(), 1L);
        expectedSchoolClass.setSchool(new School(1L));
        expectedSchoolClass.setTeacher(teacher);
        verify(schoolClassServices).update(schoolClassEq(expectedSchoolClass));
    }

    @Test
    public void updateSchoolClassWithNullFilename() throws Exception {
        updateSchoolClassWithError(new FieldNotValidException("name", "may not be null"), HttpCode.VALIDATION_ERROR,
                "sampleSchoolClass.json", "schoolClassNullName.json");
    }

    @Test
    public void updateSchoolClassWithNonexistentSchool() throws Exception {
        updateSchoolClassWithError(new SchoolNotFoundException(), HttpCode.VALIDATION_ERROR, "sampleSchoolClass.json",
                "schoolClassNonexistentSchool.json");
    }

    @Test
    public void updateSchoolClassWithNonexistentTeacher() throws Exception {
        updateSchoolClassWithError(new UserNotFoundException(), HttpCode.VALIDATION_ERROR, "sampleSchoolClass.json",
                "schoolClassNonexistentTeacher.json");
    }

    @Test
    public void updateSchoolClassNotFound() throws Exception {
        updateSchoolClassWithError(new SchoolClassNotFoundException(), HttpCode.NOT_FOUND, "sampleSchoolClass.json",
                "schoolClassErrorNonExistentSchoolClass.json");
    }

    @Test
    public void findSchoolClass() throws SchoolClassNotFoundException {
        final SchoolClass schoolClass = schoolClassWithId(schoolClass1(), 1L);
        schoolClass.getSchool().setId(1L);
        schoolClass.getTeacher().setId(1L);
        schoolClass.getTeacher().setCreatedAt(DateUtils.getAsDateTime("2015-12-12T16:07:29Z"));
        when(schoolClassServices.findById(1L)).thenReturn(schoolClass);

        final Response response = schoolClassResource.findById(1L);
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertJsonResponseWithFile(response, "sampleSchoolClassFound.json");
    }

    @Test
    public void findSchoolClassNotFound() throws SchoolClassNotFoundException {
        when(schoolClassServices.findById(1L)).thenThrow(new SchoolClassNotFoundException());

        final Response response = schoolClassResource.findById(1L);
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findBySchoolClassNoFilter() {
        final List<SchoolClass> schoolClasss = Arrays.asList(schoolClassWithId(schoolClass1(), 1L), schoolClassWithId(schoolClass2(), 2L));
        Long currentSchoolId = 1L;
        for (final SchoolClass schoolClass : schoolClasss) {
            schoolClass.getSchool().setId(currentSchoolId++);
            schoolClass.getTeacher().setId(1L);
            schoolClass.getTeacher().setCreatedAt(DateUtils.getAsDateTime("2015-12-12T16:07:29Z"));
        }

        final MultivaluedMap<String, String> multiMap = mock(MultivaluedMap.class);
        when(uriInfo.getQueryParameters()).thenReturn(multiMap);

        when(schoolClassServices.findByFilter((SchoolClassFilter) anyObject())).thenReturn(
                new PaginatedData<SchoolClass>(schoolClasss.size(), schoolClasss));

        final Response response = schoolClassResource.findByFilter();
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertJsonResponseWithFile(response, "schoolClassesAllInOnePage.json");
    }

    private void addSchoolClassWithValidationError(final Exception exceptionToBeThrown, final String requestFileName,
                                                   final String responseFileName) throws Exception {
        when(schoolClassServices.add((SchoolClass) anyObject())).thenThrow(exceptionToBeThrown);

        final Response response = schoolClassResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE, requestFileName)));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, responseFileName);
    }

    private void updateSchoolClassWithError(final Exception exceptionToBeThrown, final HttpCode expectedHttpCode,
                                            final String requestFileName,
                                            final String responseFileName) throws Exception {
        doThrow(exceptionToBeThrown).when(schoolClassServices).update(schoolClassWithId(schoolClass1(), 1L));

        final Response response = schoolClassResource.update(1L,
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

