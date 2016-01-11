/**
 * Service layer interface for the user entity
 */
package cz.muni.fi.dictatetrainer.user.services;

import javax.ejb.Local;

import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.user.exception.UserExistentException;
import cz.muni.fi.dictatetrainer.user.exception.UserNotFoundException;
import cz.muni.fi.dictatetrainer.user.model.User;
import cz.muni.fi.dictatetrainer.user.model.filter.UserFilter;

/**
 * Interface to service methods for User entity (CRUD, filtering, password altering, authentication)
 */
@Local
public interface UserServices {

	User add(User user) throws FieldNotValidException, UserExistentException;

	User findById(Long id) throws UserNotFoundException;

	void update(User user) throws FieldNotValidException, UserExistentException, UserNotFoundException;

	void updatePassword(Long id, String password) throws UserNotFoundException;

	void delete(Long id) throws UserNotFoundException;

	User findByEmail(String email) throws UserNotFoundException;

	User findByEmailAndPassword(String email, String password) throws UserNotFoundException;

	PaginatedData<User> findByFilter(UserFilter userFilter);
}