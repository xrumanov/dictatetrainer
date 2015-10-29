package cz.muni.fi.dictatetrainer.user.services.impl;

import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.utils.PasswordUtils;
import cz.muni.fi.dictatetrainer.user.exception.UserExistentException;
import cz.muni.fi.dictatetrainer.user.exception.UserNotFoundException;
import cz.muni.fi.dictatetrainer.user.model.User;
import cz.muni.fi.dictatetrainer.user.model.filter.UserFilter;
import cz.muni.fi.dictatetrainer.user.repository.UserRepository;
import cz.muni.fi.dictatetrainer.user.services.UserServices;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;

import static cz.muni.fi.dictatetrainer.commontests.user.UserArgumentMatcher.userEq;
import static cz.muni.fi.dictatetrainer.commontests.user.UsersForTestRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServicesUnitTest {

    private Validator validator;
    private UserServices userServices;

    @Mock
    private UserRepository userRepository;

    @Before
    public void initTestCase() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        MockitoAnnotations.initMocks(this);

        userServices = new UserServicesImpl();
        ((UserServicesImpl) userServices).userRepository = userRepository;
        ((UserServicesImpl) userServices).validator = validator;
    }

    @Test
    public void addUserWithNullName() {
        final User user = mrkvicka();
        user.setName(null);
        addUserWithInvalidField(user, "name");
    }

    @Test
    public void addUserWithShortName() {
        final User user = mrkvicka();
        user.setName("Jo");
        addUserWithInvalidField(user, "name");
    }

    @Test
    public void addUserWithNullEmail() {
        final User user = mrkvicka();
        user.setEmail(null);
        addUserWithInvalidField(user, "email");
    }

    @Test
    public void addUserWithInvalidEmail() {
        final User user = mrkvicka();
        user.setEmail("invalidemail");
        addUserWithInvalidField(user, "email");
    }

    @Test
    public void addUserWithNullPassword() {
        final User user = mrkvicka();
        user.setPassword(null);
        addUserWithInvalidField(user, "password");
    }

    @Test(expected = UserExistentException.class)
    public void addExistentUser() {
        when(userRepository.alreadyExists(mrkvicka())).thenThrow(new UserExistentException());

        userServices.add(mrkvicka());
    }

    @Test
    public void addValidUser() {
        when(userRepository.alreadyExists(mrkvicka())).thenReturn(false);
        when(userRepository.add(userEq(userWithEncryptedPassword(mrkvicka()))))
                .thenReturn(userWithIdAndCreatedAt(mrkvicka(), 1L));

        final User user = userServices.add(mrkvicka());
        assertThat(user.getId(), is(equalTo(1L)));
    }

    @Test(expected = UserNotFoundException.class)
    public void findUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(null);

        userServices.findById(1L);
    }

    @Test
    public void findUserById() {
        when(userRepository.findById(1L)).thenReturn(userWithIdAndCreatedAt(mrkvicka(), 1L));

        final User user = userServices.findById(1L);
        assertThat(user, is(notNullValue()));
        assertThat(user.getName(), is(equalTo(mrkvicka().getName())));
    }

    @Test
    public void updateUserWithNullName() {
        when(userRepository.findById(1L)).thenReturn(userWithIdAndCreatedAt(mrkvicka(), 1L));

        final User user = userWithIdAndCreatedAt(mrkvicka(), 1L);
        user.setName(null);

        try {
            userServices.update(user);
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("name")));
        }
    }

    @Test(expected = UserExistentException.class)
    public void updateUserExistent() throws Exception {
        when(userRepository.findById(1L)).thenReturn(userWithIdAndCreatedAt(mrkvicka(), 1L));

        final User user = userWithIdAndCreatedAt(mrkvicka(), 1L);
        when(userRepository.alreadyExists(user)).thenReturn(true);

        userServices.update(user);
    }

    @Test(expected = UserNotFoundException.class)
    public void updateUserNotFound() throws Exception {
        final User user = userWithIdAndCreatedAt(mrkvicka(), 1L);
        when(userRepository.findById(1L)).thenReturn(null);

        userServices.update(user);
    }

    @Test
    public void updateValidUser() throws Exception {
        final User user = userWithIdAndCreatedAt(mrkvicka(), 1L);
        user.setPassword(null);
        when(userRepository.findById(1L)).thenReturn(userWithIdAndCreatedAt(mrkvicka(), 1L));

        userServices.update(user);

        final User expectedUser = userWithIdAndCreatedAt(mrkvicka(), 1L);
        verify(userRepository).update(userEq(expectedUser));
    }

    @Test(expected = UserNotFoundException.class)
    public void updatePasswordUserNotFound() {
        when(userRepository.findById(1L)).thenThrow(new UserNotFoundException());

        userServices.updatePassword(1L, "123456");
    }

    @Test
    public void updatePassword() throws Exception {
        final User user = userWithIdAndCreatedAt(mrkvicka(), 1L);
        when(userRepository.findById(1L)).thenReturn(user);

        userServices.updatePassword(1L, "654654");

        final User expectedUser = userWithIdAndCreatedAt(mrkvicka(), 1L);
        expectedUser.setPassword(PasswordUtils.encryptPassword("654654"));
        verify(userRepository).update(userEq(expectedUser));
    }

    @Test(expected = UserNotFoundException.class)
    public void findUserByEmailNotFound() throws UserNotFoundException {
        when(userRepository.findByEmail(mrkvicka().getEmail())).thenReturn(null);

        userServices.findByEmail(mrkvicka().getEmail());
    }

    @Test
    public void findUserByEmail() throws UserNotFoundException {
        when(userRepository.findByEmail(mrkvicka().getEmail())).thenReturn(userWithIdAndCreatedAt(mrkvicka(), 1L));

        final User user = userServices.findByEmail(mrkvicka().getEmail());
        assertThat(user, is(notNullValue()));
        assertThat(user.getName(), is(equalTo(mrkvicka().getName())));
    }

    @Test(expected = UserNotFoundException.class)
    public void findUserByEmailAndPasswordNotFound() {
        final User user = mrkvicka();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);

        userServices.findByEmailAndPassword(user.getEmail(), user.getPassword());
    }

    @Test(expected = UserNotFoundException.class)
    public void findUserByAndPasswordEmailWithInvalidPassword() throws UserNotFoundException {
        final User user = mrkvicka();
        user.setPassword("1111");

        User userReturned = userWithIdAndCreatedAt(mrkvicka(), 1L);
        userReturned = userWithEncryptedPassword(userReturned);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(userReturned);

        userServices.findByEmailAndPassword(user.getEmail(), user.getPassword());
    }

    @Test
    public void findUserByAndPasswordEmail() throws UserNotFoundException {
        User user = mrkvicka();

        User userReturned = userWithIdAndCreatedAt(mrkvicka(), 1L);
        userReturned = userWithEncryptedPassword(userReturned);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(userReturned);

        user = userServices.findByEmailAndPassword(user.getEmail(), user.getPassword());
        assertThat(user, is(notNullValue()));
        assertThat(user.getName(), is(equalTo(mrkvicka().getName())));
    }

    @Test
    public void findUserByFilter() {
        final PaginatedData<User> users = new PaginatedData<>(1,
                Arrays.asList(userWithIdAndCreatedAt(mrkvicka(), 1L)));
        when(userRepository.findByFilter((UserFilter) anyObject())).thenReturn(users);

        final PaginatedData<User> usersReturned = userServices.findByFilter(new UserFilter());
        assertThat(usersReturned.getNumberOfRows(), is(equalTo(1)));
        assertThat(usersReturned.getRow(0).getName(), is(equalTo(mrkvicka().getName())));
    }

    private void addUserWithInvalidField(final User user, final String expectedInvalidFieldName) {
        try {
            userServices.add(user);
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo(expectedInvalidFieldName)));
        }
    }

}