package cz.muni.fi.dictatetrainer.school.services;

import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.school.exception.SchoolExistentException;
import cz.muni.fi.dictatetrainer.school.exception.SchoolNotFoundException;
import cz.muni.fi.dictatetrainer.school.model.School;

import javax.ejb.Local;
import java.util.List;

/**
 * Interface to service methods for School entity (CRUD)
 */
@Local
public interface SchoolServices {

	School add(School school) throws FieldNotValidException, SchoolExistentException;

	void update(School school) throws FieldNotValidException, SchoolNotFoundException;

	School findById(Long id) throws SchoolNotFoundException;

	List<School> findAll();

}