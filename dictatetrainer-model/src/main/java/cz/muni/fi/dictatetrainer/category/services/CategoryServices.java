package cz.muni.fi.dictatetrainer.category.services;

import java.util.List;

import javax.ejb.Local;

import cz.muni.fi.dictatetrainer.category.exception.CategoryExistentException;
import cz.muni.fi.dictatetrainer.category.exception.CategoryNotFoundException;
import cz.muni.fi.dictatetrainer.category.model.Category;
import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;

/**
 * Interface to service methods for Category entity (CRUD)
 */
@Local
public interface CategoryServices {

	Category add(Category category) throws FieldNotValidException, CategoryExistentException;

	void update(Category category) throws FieldNotValidException, CategoryNotFoundException;

	Category findById(Long id) throws CategoryNotFoundException;

	List<Category> findAll();

}