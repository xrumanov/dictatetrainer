/**
 * Implementation of the service methods for user entity
 */
package cz.muni.fi.dictatetrainer.user.services.impl;

import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.utils.PasswordUtils;
import cz.muni.fi.dictatetrainer.common.utils.ValidationUtils;
import cz.muni.fi.dictatetrainer.user.exception.UserExistentException;
import cz.muni.fi.dictatetrainer.user.exception.UserNotFoundException;
import cz.muni.fi.dictatetrainer.user.model.User;
import cz.muni.fi.dictatetrainer.user.model.filter.UserFilter;
import cz.muni.fi.dictatetrainer.user.repository.UserRepository;
import cz.muni.fi.dictatetrainer.user.services.UserServices;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
public class UserServicesImpl implements UserServices {

    @Inject
    UserRepository userRepository;

    @Inject
    Validator validator;

    @Override
    public User add(final User user) {
        validateUser(user);
        user.setPassword(PasswordUtils.encryptPassword(user.getPassword()));

        return userRepository.add(user);
    }

    @Override
    public User findById(final Long id) {
        final User user = userRepository.findById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @Override
    public void update(final User user) {
        final User existentUser = findById(user.getId());
        user.setPassword(existentUser.getPassword());

        validateUser(user);

        userRepository.update(user);
    }

    @Override
    public void updatePassword(final Long id, final String password) {
        final User user = findById(id);
        user.setPassword(PasswordUtils.encryptPassword(password));

        userRepository.update(user);
    }

    @Override
    public User findByEmail(final String email) throws UserNotFoundException {
        final User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @Override
    public User findByEmailAndPassword(final String email, final String password) {
        final User user = findByEmail(email);

        if (!user.getPassword().equals(PasswordUtils.encryptPassword(password))) {
            throw new UserNotFoundException();
        }

        return user;
    }

    @Override
    public PaginatedData<User> findByFilter(final UserFilter userFilter) {
        return userRepository.findByFilter(userFilter);
    }

    private void validateUser(final User user) throws FieldNotValidException, UserExistentException {
        if (userRepository.alreadyExists(user)) {
            throw new UserExistentException();
        }

        ValidationUtils.validateEntityFields(validator, user);
    }

}
