package cz.muni.fi.dictatetrainer.schoolclass.services;

import cz.muni.fi.dictatetrainer.category.exception.CategoryNotFoundException;
import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.schoolclass.exception.SchoolClassNotFoundException;
import cz.muni.fi.dictatetrainer.schoolclass.model.SchoolClass;
import cz.muni.fi.dictatetrainer.schoolclass.model.filter.SchoolClassFilter;

import javax.ejb.Local;

/**
 * Interface to service methods for SchoolClass entity (CRUD and filtering)
 */
@Local
public interface SchoolClassServices {

    SchoolClass add(SchoolClass schoolClass) throws FieldNotValidException, CategoryNotFoundException;

    void update(SchoolClass schoolClass) throws FieldNotValidException, CategoryNotFoundException, SchoolClassNotFoundException;

    SchoolClass findById(Long id) throws SchoolClassNotFoundException;

    PaginatedData<SchoolClass> findByFilter(SchoolClassFilter schoolClassFilter);

}
