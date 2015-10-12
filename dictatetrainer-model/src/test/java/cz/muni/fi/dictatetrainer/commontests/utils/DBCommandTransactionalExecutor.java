/**
 * Class for execute given command inside transaction
 */
package cz.muni.fi.dictatetrainer.commontests.utils;

import org.junit.Ignore;

import javax.persistence.EntityManager;

@Ignore
public class DBCommandTransactionalExecutor {

        private EntityManager em;

        public DBCommandTransactionalExecutor(final EntityManager em) {
            this.em = em;
        }

        public <T> T executeCommand(final DBCommand<T> dbCommand) {
            try {
                em.getTransaction().begin();
                final T toReturn = dbCommand.execute();
                em.getTransaction().commit();
                em.clear();
                return toReturn;
            } catch (final Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
                throw new IllegalStateException(e);
            }
        }

    }
