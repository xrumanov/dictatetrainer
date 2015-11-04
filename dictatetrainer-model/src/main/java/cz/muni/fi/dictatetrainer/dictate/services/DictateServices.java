package cz.muni.fi.dictatetrainer.dictate.services;

import cz.muni.fi.dictatetrainer.category.exception.CategoryNotFoundException;
import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.dictate.exception.DictateNotFoundException;
import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.dictate.model.filter.DictateFilter;

import javax.ejb.Local;

/**
 * Interface to service methods for Dictate entity (CRUD and filtering)
 */
@Local
public interface DictateServices {

    Dictate add(Dictate dictate) throws FieldNotValidException, CategoryNotFoundException;

    void update(Dictate dictate) throws FieldNotValidException, CategoryNotFoundException, DictateNotFoundException;

    Dictate findById(Long id) throws DictateNotFoundException;

    PaginatedData<Dictate> findByFilter(DictateFilter dictateFilter);

}
