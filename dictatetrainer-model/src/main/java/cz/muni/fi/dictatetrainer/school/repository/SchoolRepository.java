package cz.muni.fi.dictatetrainer.school.repository;

import cz.muni.fi.dictatetrainer.common.repository.GenericRepository;
import cz.muni.fi.dictatetrainer.school.model.School;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Repository layer implementation for the entity School
 */
@Stateless
public class SchoolRepository extends GenericRepository<School> {

    @PersistenceContext
    EntityManager em;

    @Override
    protected Class<School> getPersistentClass() {
        return School.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public boolean alreadyExists(final School school) {
        return alreadyExists("name", school.getName(), school.getId());
    }

}