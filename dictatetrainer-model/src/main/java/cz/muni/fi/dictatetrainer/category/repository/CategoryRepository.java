package cz.muni.fi.dictatetrainer.category.repository;

import cz.muni.fi.dictatetrainer.category.model.Category;
import cz.muni.fi.dictatetrainer.common.repository.GenericRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Repository layer implementation for the entity Category
 */
@Stateless
public class CategoryRepository extends GenericRepository<Category> {

    @PersistenceContext
    EntityManager em;

    @Override
    protected Class<Category> getPersistentClass() {
        return Category.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public boolean alreadyExists(final Category category) {
        return alreadyExists("name", category.getName(), category.getId());
    }

}