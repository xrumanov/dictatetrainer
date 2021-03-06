package cz.muni.fi.dictatetrainer.dictate.repository;

import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.repository.GenericRepository;
import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.dictate.model.filter.DictateFilter;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Repository layer implementation for the entity Dictate
 */
@Stateless
public class DictateRepository extends GenericRepository<Dictate> {
    @PersistenceContext
    EntityManager em;

    @Override
    protected Class<Dictate> getPersistentClass() {
        return Dictate.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PaginatedData<Dictate> findByFilter(final DictateFilter dictateFilter) {
        final StringBuilder clause = new StringBuilder("Where e.id is not null");
        final Map<String, Object> queryParameters = new HashMap<>();
        if (dictateFilter.getUploaderId() != null) {
            clause.append(" AND e.uploader.id = :uploaderId");
            queryParameters.put("uploaderId", dictateFilter.getUploaderId());
        }
        if (dictateFilter.getCategoryId() != null) {
            clause.append(" AND e.category.id = :categoryId");
            queryParameters.put("categoryId", dictateFilter.getCategoryId());
        }

        if (dictateFilter.getFilename() != null) {
            clause.append(" AND e.filename = :filename");
            queryParameters.put("filename", dictateFilter.getFilename());
        }

        return findByParameters(clause.toString(), dictateFilter.getPaginationData(), queryParameters, "filename ASC");
    }

}