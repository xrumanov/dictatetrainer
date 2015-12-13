package cz.muni.fi.dictatetrainer.school.resource;

import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.model.HttpCode;
import cz.muni.fi.dictatetrainer.commontests.utils.ResourceDefinitions;
import cz.muni.fi.dictatetrainer.school.exception.SchoolExistentException;
import cz.muni.fi.dictatetrainer.school.exception.SchoolNotFoundException;
import cz.muni.fi.dictatetrainer.school.model.School;
import cz.muni.fi.dictatetrainer.school.services.SchoolServices;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;

import static cz.muni.fi.dictatetrainer.commontests.school.SchoolsForTestRepository.*;
import static cz.muni.fi.dictatetrainer.commontests.utils.FileTestNameUtils.getPathFileRequest;
import static cz.muni.fi.dictatetrainer.commontests.utils.FileTestNameUtils.getPathFileResponse;
import static cz.muni.fi.dictatetrainer.commontests.utils.JsonTestUtils.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class SchoolResourceUnitTest {

    private SchoolResource schoolResource;

    private static final String PATH_RESOURCE = ResourceDefinitions.SCHOOL.getResourceName();

    @Mock
    private SchoolServices schoolServices;

    @Before
    public void initTestCase() {
        MockitoAnnotations.initMocks(this);
        schoolResource = new SchoolResource();

        schoolResource.schoolServices = schoolServices;
        schoolResource.schoolJsonConverter = new SchoolJsonConverter();
    }

    @Test
    public void addValidSchool() {
        when(schoolServices.add(school1())).thenReturn(schoolWithId(school1(), 1L));

        final Response response = schoolResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE,
                "newSchool.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.CREATED.getCode())));
        assertJsonMatchesExpectedJson(response.getEntity().toString(), "{\"id\": 1}");
    }

    @Test
    public void addExistentSchool() {
        when(schoolServices.add(school1())).thenThrow(new SchoolExistentException());

        final Response response = schoolResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE,
                "newSchool.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "schoolAlreadyExists.json");
    }

    @Test
    public void addSchoolWithNullName() {
        when(schoolServices.add(new School())).thenThrow(new FieldNotValidException("name", "may not be null"));

        final Response response = schoolResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE,
                "schoolWithNullName.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "schoolErrorNullName.json");
    }

    @Test
    public void updateValidSchool() {
        final Response response = schoolResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "school.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertThat(response.getEntity().toString(), is(equalTo("")));

        verify(schoolServices).update(schoolWithId(school2(), 1L));
    }

    @Test
    public void updateSchoolWithNameBelongingToOtherSchool() {
        doThrow(new SchoolExistentException()).when(schoolServices).update(schoolWithId(school2(), 1L));

        final Response response = schoolResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "school.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "schoolAlreadyExists.json");
    }

    @Test
    public void updateSchoolWithNullName() {
        doThrow(new FieldNotValidException("name", "may not be null")).when(schoolServices).update(
                schoolWithId(new School(), 1L));

        final Response response = schoolResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "schoolWithNullName.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "schoolErrorNullName.json");
    }

    @Test
    public void updateSchoolNotFound() {
        doThrow(new SchoolNotFoundException()).when(schoolServices).update(schoolWithId(school2(), 2L));

        final Response response = schoolResource.update(2L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "school.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
        assertJsonResponseWithFile(response, "schoolNotFound.json");
    }

    @Test
    public void findSchool() {
        when(schoolServices.findById(1L)).thenReturn(schoolWithId(school1(), 1L));

        final Response response = schoolResource.findById(1L);
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertJsonResponseWithFile(response, "schoolFound.json");
    }

    @Test
    public void findSchoolNotFound() {
        when(schoolServices.findById(1L)).thenThrow(new SchoolNotFoundException());

        final Response response = schoolResource.findById(1L);
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
    }

    @Test
    public void findAllNoSchool() {
        when(schoolServices.findAll()).thenReturn(new ArrayList<>());

        final Response response = schoolResource.findAll();
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertJsonResponseWithFile(response, "emptyListOfSchools.json");
    }

    @Test
    public void findAllTwoCategories() {
        when(schoolServices.findAll()).thenReturn(
                Arrays.asList(schoolWithId(school1(), 1L), schoolWithId(school2(), 2L)));

        final Response response = schoolResource.findAll();
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertJsonResponseWithFile(response, "twoSchools.json");
    }

    private void assertJsonResponseWithFile(final Response response, final String fileName) {
        assertJsonMatchesFileContent(response.getEntity().toString(), getPathFileResponse(PATH_RESOURCE, fileName));
    }

}