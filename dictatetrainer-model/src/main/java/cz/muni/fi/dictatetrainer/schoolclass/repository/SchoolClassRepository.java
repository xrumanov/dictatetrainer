package cz.muni.fi.dictatetrainer.schoolclass.repository;

import cz.muni.fi.dictatetrainer.schoolclass.model.SchoolClass;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.repository.GenericRepository;
import cz.muni.fi.dictatetrainer.schoolclass.model.filter.SchoolClassFilter;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Repository layer implementation for the entity SchoolClass
 */
@Stateless
public class SchoolClassRepository extends GenericRepository<SchoolClass> {
    @PersistenceContext
    EntityManager em;

    @Override
    protected Class<SchoolClass> getPersistentClass() {
        return SchoolClass.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PaginatedData<SchoolClass> findByFilter(final SchoolClassFilter schoolClassFilter) {
        final StringBuilder clause = new StringBuilder("Where e.id is not null");
        final Map<String, Object> queryParameters = new HashMap<>();
        if (schoolClassFilter.getTeacherId() != null) {
            clause.append(" AND e.teacher.id = :teacherId");
            queryParameters.put("teacherId", schoolClassFilter.getTeacherId());
        }

        if (schoolClassFilter.getSchoolId() != null) {
            clause.append(" AND e.school.id = :schoolId");
            queryParameters.put("schoolId", schoolClassFilter.getSchoolId());
        }

        return findByParameters(clause.toString(), schoolClassFilter.getPaginationData(), queryParameters, "id ASC");
    }

}