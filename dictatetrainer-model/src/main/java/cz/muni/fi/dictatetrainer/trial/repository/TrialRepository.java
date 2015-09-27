package cz.muni.fi.dictatetrainer.trial.repository;

import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.repository.GenericRepository;
import cz.muni.fi.dictatetrainer.trial.model.Trial;
import cz.muni.fi.dictatetrainer.trial.model.filter.TrialFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jrumanov on 8/30/15.
 */
public class TrialRepository extends GenericRepository<Trial>{

    @PersistenceContext
    EntityManager em;

    @Override
    protected Class<Trial> getPersistentClass() {
        return Trial.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PaginatedData<Trial> findByFilter(final TrialFilter trialFilter) {
        final StringBuilder clause = new StringBuilder("Where e.id is not null");
        final Map<String, Object> queryParameters = new HashMap<>();
        if (trialFilter.getDictateId() != null) {
            clause.append(" And e.dictate.id = :dictateId");
            queryParameters.put("dictateId", trialFilter.getDictateId());
        }
        if (trialFilter.getStudentId() != null) {
            clause.append(" And e.student.id = :studentId");
            queryParameters.put("studentId", trialFilter.getStudentId());
        }
        if (trialFilter.getStartDate() != null) {
            clause.append(" And e.createdAt >= :startDate");
            queryParameters.put("startDate", trialFilter.getStartDate());
        }
        if (trialFilter.getEndDate() != null) {
            clause.append(" And e.createdAt <= :endDate");
            queryParameters.put("endDate", trialFilter.getEndDate());
        }

        return findByParameters(clause.toString(), trialFilter.getPaginationData(), queryParameters, "performed DESC");
    }

}
