/**
 * Class that represents persistence layer of the user management
 */
package cz.muni.fi.dictatetrainer.user.repository;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.repository.GenericRepository;
import cz.muni.fi.dictatetrainer.user.model.User;
import cz.muni.fi.dictatetrainer.user.model.filter.UserFilter;

@Stateless
public class UserRepository extends GenericRepository<User> {

	@PersistenceContext
	EntityManager em;

	@Override
	protected Class<User> getPersistentClass() {
		return User.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public boolean alreadyExists(final User user) {
		return alreadyExists("email", user.getEmail(), user.getId());
	}

	public User findByEmail(final String email) {
		try {
			return (User) em.createQuery("Select e From User e where e.email = :email")
					.setParameter("email", email)
					.getSingleResult();
		} catch (final NoResultException e) {
			return null;
		}
	}

	public PaginatedData<User> findByFilter(final UserFilter userFilter) {
		final StringBuilder clause = new StringBuilder("WHERE e.id is not null");
		final Map<String, Object> queryParameters = new HashMap<>();
		if (userFilter.getName() != null) {
			clause.append(" And Upper(e.name) Like Upper(:name)");
			queryParameters.put("name", "%" + userFilter.getName() + "%");
		}
		if (userFilter.getUserType() != null) {
			clause.append(" And e.userType = :userType");
			queryParameters.put("userType", userFilter.getUserType());
		}
		return findByParameters(clause.toString(), userFilter.getPaginationData(), queryParameters, "name ASC");
	}

}