/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.iu.likeherotozero.dao;

import de.iu.likeherotozero.model.Scientist;
import de.iu.likeherotozero.persistence.PersistenceManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
/**
 *
 * @author Iskender Dumlu
 */
@ApplicationScoped
public class ScientistDao {

    @Inject
    private PersistenceManager persistenceManager;
    
    public Scientist findByEmail(String email) {
        EntityManager entityManager
                = persistenceManager.createEntityManager();

        try {
            List<Scientist> scientists = entityManager.createQuery(
                    "SELECT s FROM Scientist s "
                    + "WHERE LOWER(s.email) = LOWER(:email)",
                    Scientist.class
            )
                    .setParameter("email", email)
                    .setMaxResults(1)
                    .getResultList();

            if (scientists.isEmpty()) {
                return null;
            }

            return scientists.get(0);
        } finally {
            entityManager.close();
        }
    }

    public Scientist save(Scientist scientist) {
        EntityManager entityManager
                = persistenceManager.createEntityManager();

        EntityTransaction transaction
                = entityManager.getTransaction();

        try {
            transaction.begin();

            Scientist savedScientist;

            if (scientist.getId() == null) {
                entityManager.persist(scientist);
                savedScientist = scientist;
            } else {
                savedScientist
                        = entityManager.merge(scientist);
            }

            transaction.commit();
            return savedScientist;
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        } finally {
            entityManager.close();
        }
    }
}
