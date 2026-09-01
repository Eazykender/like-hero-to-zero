/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.iu.likeherotozero.dao;

import de.iu.likeherotozero.model.EmissionRecord;
import de.iu.likeherotozero.persistence.PersistenceManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;
import jakarta.persistence.EntityTransaction;
/**
 *
 * @author Iskender Dumlu
 */
@ApplicationScoped
public class EmissionRecordDao {

    @Inject
    private PersistenceManager persistenceManager;

    public List<EmissionRecord> findLatestForAllCountries() {
        EntityManager entityManager
                = persistenceManager.createEntityManager();

        try {
            return entityManager.createQuery(
                    "SELECT e FROM EmissionRecord e "
                    + "WHERE e.year = ("
                    + "SELECT MAX(e2.year) FROM EmissionRecord e2 "
                    + "WHERE e2.country = e.country) "
                    + "ORDER BY e.country.name",
                    EmissionRecord.class
            ).getResultList();
        } finally {
            entityManager.close();
        }
    }
    public EmissionRecord findByCountryAndYear(
            Long countryId,
            Integer year) {

        EntityManager entityManager
                = persistenceManager.createEntityManager();

        try {
            List<EmissionRecord> records
                    = entityManager.createQuery(
                            "SELECT e FROM EmissionRecord e "
                            + "WHERE e.country.id = :countryId "
                            + "AND e.year = :year",
                            EmissionRecord.class
                    )
                            .setParameter("countryId", countryId)
                            .setParameter("year", year)
                            .setMaxResults(1)
                            .getResultList();

            if (records.isEmpty()) {
                return null;
            }

            return records.get(0);
        } finally {
            entityManager.close();
        }
    }

    public EmissionRecord save(EmissionRecord emissionRecord) {
        EntityManager entityManager
                = persistenceManager.createEntityManager();

        EntityTransaction transaction
                = entityManager.getTransaction();

        try {
            transaction.begin();

            EmissionRecord savedRecord;

            if (emissionRecord.getId() == null) {
                entityManager.persist(emissionRecord);
                savedRecord = emissionRecord;
            } else {
                savedRecord
                        = entityManager.merge(emissionRecord);
            }

            transaction.commit();
            return savedRecord;
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
