package cz.muni.fi.dictatetrainer.common.repository;

import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.model.filter.PaginationData;
import cz.muni.fi.dictatetrainer.dictate.model.Dictate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Generic class that encapsulates the methods shared between by all classes in repository layer
 */
public abstract class GenericRepository<T> {

    //return type of the class
    protected abstract Class<T> getPersistentClass();

    protected abstract EntityManager getEntityManager();

    public T add(final T entity) {
        getEntityManager().persist(entity);
        return entity;
    }

    public T findById(final Long id) {
        if (id == null) {
            return null;
        }
        return getEntityManager().find(getPersistentClass(), id);
    }

    public void update(final T entity) {
        getEntityManager().merge(entity);
    }

    /**
     * Finds all the entities sorted by given field
     *
     * @param orderField field on which the entities will be sorted
     * @return List of entities
     */
    @SuppressWarnings("unchecked")
    public List<T> findAll(final String orderField) {
        return getEntityManager().createQuery(
                "Select e From " + getPersistentClass().getSimpleName() + " e Order by e." + orderField)
                .getResultList();
    }

    /**
     * Tests if the entity already exists in the DB
     *
     * @param propertyName  name of the unique property of the entity
     * @param propertyValue value of the property that will be tested for uniqueness
     * @param id            id of the inserted entity
     * @return true if the entity already exists with different id
     */
    public boolean alreadyExists(final String propertyName, final String propertyValue, final Long id) {
        final StringBuilder jpql = new StringBuilder();
        jpql.append("Select 1 From " + getPersistentClass().getSimpleName() + " e where e." + propertyName
                + " = :propertyValue");
        if (id != null) {
            jpql.append(" and e.id != :id");
        }

        final Query query = getEntityManager().createQuery(jpql.toString());
        query.setParameter("propertyValue", propertyValue);
        if (id != null) {
            query.setParameter("id", id);
        }

        return query.setMaxResults(1).getResultList().size() > 0;
    }

    /**
     * Tests if the entity already exists by the given id
     *
     * @param id id of an entity
     * @return true if exists
     */
    public boolean existsById(final Long id) {
        return getEntityManager()
                .createQuery("Select 1 From " + getPersistentClass().getSimpleName() + " e where e.id = :id")
                .setParameter("id", id)
                .setMaxResults(1)
                .getResultList().size() > 0;
    }

    /**
     * Finds all the entities that match given clause
     *
     * @param clause                        JPQL command
     * @param paginationData                pagination settings
     * @param queryParameters               map of parameters for a query
     * @param defaultSortFieldWithDirection field on that sort will be performed
     * @return Paginated entities
     */
    @SuppressWarnings("unchecked")
    protected PaginatedData<T> findByParameters(final String clause, final PaginationData paginationData,
                                                final Map<String, Object> queryParameters, final String defaultSortFieldWithDirection) {
        final String clauseSort = "Order by e." + getSortField(paginationData, defaultSortFieldWithDirection);
        final Query queryEntities = getEntityManager().createQuery(
                "Select e From " + getPersistentClass().getSimpleName()
                        + " e " + clause + " " + clauseSort);
        applyQueryParametersOnQuery(queryParameters, queryEntities);
        applyPaginationOnQuery(paginationData, queryEntities);

        final List<T> entities = queryEntities.getResultList();

        return new PaginatedData<T>(countWithFilter(clause, queryParameters), entities);
    }


    /**
     * Count number of results using filter
     * @param clause JPQL command
     * @param queryParameters list of parameters for a query
     * @return number of entities returned
     */
    private int countWithFilter(final String clause, final Map<String, Object> queryParameters) {
        final Query queryCount = getEntityManager().createQuery(
                "Select count(e) From " + getPersistentClass().getSimpleName() + " e " + clause);
        applyQueryParametersOnQuery(queryParameters, queryCount);
        return ((Long) queryCount.getSingleResult()).intValue();
    }

    /**
     * Helper method that sets the first and max result for a query
     * @param paginationData pagination settings
     * @param query given query
     */
    private void applyPaginationOnQuery(final PaginationData paginationData, final Query query) {
        if (paginationData != null) {
            query.setFirstResult(paginationData.getFirstResult());
            query.setMaxResults(paginationData.getMaxResults());
        }
    }

    /**
     * Helper method that returns sort field of pagination
     * @param paginationData pagination settings
     * @param defaultSortField default field on which sorting is performed
     * @return field on which sorting is performed
     */
    private String getSortField(final PaginationData paginationData, final String defaultSortField) {
        if (paginationData == null || paginationData.getOrderField() == null) {
            return defaultSortField;
        }
        return paginationData.getOrderField() + " " + getSortDirection(paginationData);
    }

    /**
     * Helper method that return sorting direction for sort field
     * @param paginationData pagination settings
     * @return direction of sorting
     */
    private String getSortDirection(final PaginationData paginationData) {
        return paginationData.isAscending() ? "ASC" : "DESC";
    }

    /**
     * Helper method that applies all the given query parameters on query
     * @param queryParameters parameters for a query
     * @param query query
     */
    private void applyQueryParametersOnQuery(final Map<String, Object> queryParameters, final Query query) {
        for (final Entry<String, Object> entryMap : queryParameters.entrySet()) {
            query.setParameter(entryMap.getKey(), entryMap.getValue());
        }
    }
}