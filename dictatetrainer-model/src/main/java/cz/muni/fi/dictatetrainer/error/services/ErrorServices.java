package cz.muni.fi.dictatetrainer.error.services;

import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.dictate.exception.DictateNotFoundException;
import cz.muni.fi.dictatetrainer.error.exception.ErrorNotFoundException;
import cz.muni.fi.dictatetrainer.error.model.Error;
import cz.muni.fi.dictatetrainer.error.model.filter.ErrorFilter;
import cz.muni.fi.dictatetrainer.user.exception.UserNotFoundException;

import javax.ejb.Local;
import java.lang.*;

/**
 * Interface to service methods for Error entity (CRUD and filtering)
 */
@Local
public interface ErrorServices {

    Error add(Error error) throws FieldNotValidException, DictateNotFoundException, UserNotFoundException;

    void update(Error error) throws FieldNotValidException, DictateNotFoundException, UserNotFoundException, ErrorNotFoundException;

    Error findById(Long id) throws ErrorNotFoundException;

    PaginatedData<Error> findByFilter(ErrorFilter errorFilter);

}