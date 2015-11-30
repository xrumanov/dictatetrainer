package cz.muni.fi.dictatetrainer.category.resource;

import cz.muni.fi.dictatetrainer.category.exception.CategoryExistentException;
import cz.muni.fi.dictatetrainer.category.exception.CategoryNotFoundException;
import cz.muni.fi.dictatetrainer.category.model.Category;
import cz.muni.fi.dictatetrainer.category.services.CategoryServices;
import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.model.HttpCode;
import cz.muni.fi.dictatetrainer.commontests.utils.ResourceDefinitions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;

import static cz.muni.fi.dictatetrainer.commontests.category.CategoriesForTestRepository.*;
import static cz.muni.fi.dictatetrainer.commontests.utils.FileTestNameUtils.getPathFileRequest;
import static cz.muni.fi.dictatetrainer.commontests.utils.FileTestNameUtils.getPathFileResponse;
import static cz.muni.fi.dictatetrainer.commontests.utils.JsonTestUtils.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class CategoryResourceUnitTest {

    private CategoryResource categoryResource;

    private static final String PATH_RESOURCE = ResourceDefinitions.CATEGORY.getResourceName();

    @Mock
    private CategoryServices categoryServices;

    @Before
    public void initTestCase() {
        MockitoAnnotations.initMocks(this);
        categoryResource = new CategoryResource();

        categoryResource.categoryServices = categoryServices;
        categoryResource.categoryJsonConverter = new CategoryJsonConverter();
    }

    @Test
    public void addValidCategory() {
        when(categoryServices.add(interpunkce())).thenReturn(categoryWithId(interpunkce(), 1L));

        final Response response = categoryResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE,
                "newCategory.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.CREATED.getCode())));
        assertJsonMatchesExpectedJson(response.getEntity().toString(), "{\"id\": 1}");
    }

    @Test
    public void addExistentCategory() {
        when(categoryServices.add(interpunkce())).thenThrow(new CategoryExistentException());

        final Response response = categoryResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE,
                "newCategory.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "categoryAlreadyExists.json");
    }

    @Test
    public void addCategoryWithNullName() {
        when(categoryServices.add(new Category())).thenThrow(new FieldNotValidException("name", "may not be null"));

        final Response response = categoryResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE,
                "categoryWithNullName.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "categoryErrorNullName.json");
    }

    @Test
    public void updateValidCategory() {
        final Response response = categoryResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "category.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertThat(response.getEntity().toString(), is(equalTo("")));

        verify(categoryServices).update(categoryWithId(vyjmenovanaSlovaCat(), 1L));
    }

    @Test
    public void updateCategoryWithNameBelongingToOtherCategory() {
        doThrow(new CategoryExistentException()).when(categoryServices).update(categoryWithId(vyjmenovanaSlovaCat(), 1L));

        final Response response = categoryResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "category.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "categoryAlreadyExists.json");
    }

    @Test
    public void updateCategoryWithNullName() {
        doThrow(new FieldNotValidException("name", "may not be null")).when(categoryServices).update(
                categoryWithId(new Category(), 1L));

        final Response response = categoryResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "categoryWithNullName.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "categoryErrorNullName.json");
    }

    @Test
    public void updateCategoryNotFound() {
        doThrow(new CategoryNotFoundException()).when(categoryServices).update(categoryWithId(vyjmenovanaSlovaCat(), 2L));

        final Response response = categoryResource.update(2L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "category.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
        assertJsonResponseWithFile(response, "categoryNotFound.json");
    }

    @Test
    public void findCategory() {
        when(categoryServices.findById(1L)).thenReturn(categoryWithId(interpunkce(), 1L));

        final Response response = categoryResource.findById(1L);
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertJsonResponseWithFile(response, "categoryFound.json");
    }

    @Test
    public void findCategoryNotFound() {
        when(categoryServices.findById(1L)).thenThrow(new CategoryNotFoundException());

        final Response response = categoryResource.findById(1L);
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
    }

    @Test
    public void findAllNoCategory() {
        when(categoryServices.findAll()).thenReturn(new ArrayList<>());

        final Response response = categoryResource.findAll();
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertJsonResponseWithFile(response, "emptyListOfCategories.json");
    }

    @Test
    public void findAllTwoCategories() {
        when(categoryServices.findAll()).thenReturn(
                Arrays.asList(categoryWithId(interpunkce(), 1L), categoryWithId(velkaPismena(), 2L)));

        final Response response = categoryResource.findAll();
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertJsonResponseWithFile(response, "twoCategories.json");
    }

    private void assertJsonResponseWithFile(final Response response, final String fileName) {
        assertJsonMatchesFileContent(response.getEntity().toString(), getPathFileResponse(PATH_RESOURCE, fileName));
    }

}
