package cz.muni.fi.dictatetrainer.error.repository;

import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.repository.GenericRepository;
import cz.muni.fi.dictatetrainer.error.model.filter.ErrorFilter;
import cz.muni.fi.dictatetrainer.error.model.Error;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

@Stateless
public class ErrorRepository extends GenericRepository<Error> {
    @PersistenceContext
    EntityManager em;

    @Override
    protected Class<Error> getPersistentClass() {
        return Error.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PaginatedData<Error> findByFilter(final ErrorFilter errorFilter) {
        final StringBuilder clause = new StringBuilder("Where e.id is not null");
        final Map<String, Object> queryParameters = new HashMap<>();
        if (errorFilter.getStudentId() != null) {
            clause.append(" AND e.user.id = :studentId");
            queryParameters.put("studentId", errorFilter.getStudentId());
        }
        if (errorFilter.getDictateId() != null) {
            clause.append(" AND e.dictate.id = :dictateId");
            queryParameters.put("dictateId", errorFilter.getDictateId());
        }

        return findByParameters(clause.toString(), errorFilter.getPaginationData(), queryParameters, "id ASC");
    }

}