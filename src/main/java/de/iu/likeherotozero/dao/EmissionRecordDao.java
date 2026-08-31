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
}