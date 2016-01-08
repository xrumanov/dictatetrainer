package cz.muni.fi.dictatetrainer.user.resource;

import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.model.HttpCode;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.commontests.utils.ResourceDefinitions;
import cz.muni.fi.dictatetrainer.user.exception.UserExistentException;
import cz.muni.fi.dictatetrainer.user.exception.UserNotFoundException;
import cz.muni.fi.dictatetrainer.user.model.User;
import cz.muni.fi.dictatetrainer.user.model.User.Roles;
import cz.muni.fi.dictatetrainer.user.model.filter.UserFilter;
import cz.muni.fi.dictatetrainer.user.services.UserServices;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static cz.muni.fi.dictatetrainer.commontests.user.UserArgumentMatcher.userEq;
import static cz.muni.fi.dictatetrainer.commontests.user.UserTestUtils.getJsonWithEmailAndPassword;
import static cz.muni.fi.dictatetrainer.commontests.user.UserTestUtils.getJsonWithPassword;
import static cz.muni.fi.dictatetrainer.commontests.user.UsersForTestRepository.*;
import static cz.muni.fi.dictatetrainer.commontests.utils.FileTestNameUtils.getPathFileRequest;
import static cz.muni.fi.dictatetrainer.commontests.utils.FileTestNameUtils.getPathFileResponse;
import static cz.muni.fi.dictatetrainer.commontests.utils.JsonTestUtils.assertJsonMatchesFileContent;
import static cz.muni.fi.dictatetrainer.commontests.utils.JsonTestUtils.readJsonFile;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class UserResourceUnitTest {

    private UserResource userResource;

    @Mock
    private UserServices userServices;

    @Mock
    private UriInfo uriInfo;

    @Mock
    private SecurityContext securityContext;

    private static final String PATH_RESOURCE = ResourceDefinitions.USER.getResourceName();

    @Before
    public void initTestCase() {
        MockitoAnnotations.initMocks(this);

        userResource = new UserResource();

        userResource.userJsonConverter = new UserJsonConverter();
        userResource.userServices = userServices;
        userResource.uriInfo = uriInfo;
        userResource.securityContext = securityContext;
    }

    @Test
    public void addValidStudent() {
        when(userServices.add(userEq(mrkvicka()))).thenReturn(userWithIdAndCreatedAt(mrkvicka(), 1L));

        final Response response = userResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE,
                "studentMrkvicka.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.CREATED.getCode())));
        assertJsonResponseWithFile(response, "studentMrkvickaAdded.json");
    }

    @Test
    public void addValidTeacher() {
        when(userServices.add(userEq(admin()))).thenReturn(userWithIdAndCreatedAt(admin(), 1L));

        final Response response = userResource
                .add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "teacherAdmin.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.CREATED.getCode())));
        assertJsonResponseWithFile(response, "teacherAdded.json");
    }

    @Test
    public void addExistentUser() {
        when(userServices.add(userEq(mrkvicka()))).thenThrow(new UserExistentException());

        final Response response = userResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE,
                "studentMrkvicka.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "userAlreadyExists.json");
    }

    @Test
    public void addUserWithNullName() {
        when(userServices.add((User) anyObject())).thenThrow(new FieldNotValidException("name", "may not be null"));

        final Response response = userResource
                .add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "studentWithNullName.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "userNullName.json");
    }

    @Test
    public void updateValidStudent() {
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(true);

        final Response response = userResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "updateStudentMrkvicka.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertThat(response.getEntity().toString(), is(equalTo("")));

        final User expectedUser = userWithIdAndCreatedAt(mrkvicka(), 1L);
        expectedUser.setPassword(null);
        verify(userServices).update(userEq(expectedUser));
    }

    @Test
    public void updateValidStudentLoggedAsStudentToBeUpdated() {
        setUpPrincipalUser(userWithIdAndCreatedAt(mrkvicka(), 1L));
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(false);

        final Response response = userResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "updateStudentMrkvicka.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertThat(response.getEntity().toString(), is(equalTo("")));

        final User expectedUser = userWithIdAndCreatedAt(mrkvicka(), 1L);
        expectedUser.setPassword(null);
        verify(userServices).update(userEq(expectedUser));
    }

    @Test
    public void updateValidStudentLoggedAsOtherStudent() {
        setUpPrincipalUser(userWithIdAndCreatedAt(gates(), 2L));
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(false);

        final Response response = userResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "updateStudentMrkvicka.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.FORBIDDEN.getCode())));
    }

    @Test
    public void updateValidTeacher() {
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(true);

        final Response response = userResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "updateTeacherAdmin.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertThat(response.getEntity().toString(), is(equalTo("")));

        final User expectedUser = userWithIdAndCreatedAt(admin(), 1L);
        expectedUser.setPassword(null);
        verify(userServices).update(userEq(expectedUser));
    }

    @Test
    public void updateUserWithEmailBelongingToOtherUser() {
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(true);

        doThrow(new UserExistentException()).when(userServices).update(userWithIdAndCreatedAt(mrkvicka(), 1L));

        final Response response = userResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "updateStudentMrkvicka.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "userAlreadyExists.json");
    }

    @Test
    public void updateUserWithNullName() {
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(true);

        doThrow(new FieldNotValidException("name", "may not be null")).when(userServices).update(
                userWithIdAndCreatedAt(mrkvicka(), 1L));

        final Response response = userResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "studentWithNullName.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "userNullName.json");
    }

    @Test
    public void updateUserNotFound() {
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(true);
        doThrow(new UserNotFoundException()).when(userServices).update(userWithIdAndCreatedAt(mrkvicka(), 2L));

        final Response response = userResource.update(2L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "updateStudentMrkvicka.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
    }

    @Test
    public void updateUserPassword() {
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(true);

        final Response response = userResource.updatePassword(1L, getJsonWithPassword("123456"));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertThat(response.getEntity().toString(), is(equalTo("")));

        verify(userServices).updatePassword(1L, "123456");
    }

    @Test
    public void updateStudentPasswordLoggedAsStudentToBeUpdated() {
        setUpPrincipalUser(userWithIdAndCreatedAt(mrkvicka(), 1L));
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(false);

        final Response response = userResource.updatePassword(1L, getJsonWithPassword("123456"));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertThat(response.getEntity().toString(), is(equalTo("")));

        verify(userServices).updatePassword(1L, "123456");
    }

    @Test
    public void updateStudentPasswordLoggedAsOtherStudent() {
        setUpPrincipalUser(userWithIdAndCreatedAt(gates(), 2L));
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(false);

        final Response response = userResource.updatePassword(1L, getJsonWithPassword("123456"));
        assertThat(response.getStatus(), is(equalTo(HttpCode.FORBIDDEN.getCode())));
    }

//    @Test
//    public void findStudentById() {
//        final User user = userWithIdAndCreatedAt(mrkvicka(), 1L);
//        when(userServices.findById(1L)).thenReturn(userWithIdAndCreatedAt(mrkvicka(), 1L));
//        user.getSchoolClass().setId(1L);
//
//        final Response response = userResource.findById(1L);
//        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
//        assertJsonResponseWithFile(response, "studentMrkvickaFound.json");
//    }

    @Test
    public void findUserByIdNotFound() {
        when(userServices.findById(1L)).thenThrow(new UserNotFoundException());

        final Response response = userResource.findById(1L);
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
    }

    @Test
    public void findTeacherByEmailAndPassword() {
        when(userServices.findByEmailAndPassword(admin().getEmail(), admin().getPassword())).thenReturn(
                userWithIdAndCreatedAt(admin(), 1L));

        final Response response = userResource.findByEmailAndPassword(getJsonWithEmailAndPassword(admin().getEmail(),
                admin().getPassword()));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertJsonResponseWithFile(response, "teacherAdminFound.json");
    }

    @Test
    public void findUserByEmailAndPasswordNotFound() {
        when(userServices.findByEmailAndPassword(admin().getEmail(), admin().getPassword())).thenThrow(
                new UserNotFoundException());

        final Response response = userResource.findByEmailAndPassword(getJsonWithEmailAndPassword(admin().getEmail(),
                admin()
                        .getPassword()));
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
    }

//    @SuppressWarnings("unchecked")
//    @Test
//    public void findByFilterNoFilter() {
//        final List<User> users = new ArrayList<>();
//        final List<User> allUsers = allUsers();
//        for (int i = 1; i <= allUsers.size(); i++) {
//            users.add(userWithIdAndCreatedAt(allUsers.get(i - 1), new Long(i)));
//        }
//
//        final MultivaluedMap<String, String> multiMap = mock(MultivaluedMap.class);
//        when(uriInfo.getQueryParameters()).thenReturn(multiMap);
//
//        when(userServices.findByFilter((UserFilter) anyObject())).thenReturn(
//                new PaginatedData<User>(users.size(), users));
//
//        final Response response = userResource.findByFilter();
//        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
//        assertJsonResponseWithFile(response, "usersAllInOnePage.json");
//    }

    private void setUpPrincipalUser(final User user) {
        final Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(user.getEmail());

        when(securityContext.getUserPrincipal()).thenReturn(principal);
        when(userServices.findByEmail(user.getEmail())).thenReturn(user);
    }

    private void assertJsonResponseWithFile(final Response response, final String fileName) {
        assertJsonMatchesFileContent(response.getEntity().toString(), getPathFileResponse(PATH_RESOURCE, fileName));
    }

}
