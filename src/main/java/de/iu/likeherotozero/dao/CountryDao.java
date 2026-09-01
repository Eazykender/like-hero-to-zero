/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.iu.likeherotozero.dao;

import de.iu.likeherotozero.model.Country;
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
public class CountryDao {
    
    @Inject
    private PersistenceManager persistenceManager;
    
    public List<Country> findAll() {
        EntityManager entityManager
                = persistenceManager.createEntityManager();
        try {
            return entityManager.createQuery(
                    "SELECT c FROM Country c ORDER BY c.name",
                    Country.class
            ).getResultList();
        }finally {
            entityManager.close();
        }
    }
public Country findById(Long id) {
    EntityManager entityManager
            = persistenceManager.createEntityManager();

    try {
        return entityManager.find(Country.class, id);
    } finally {
        entityManager.close();
    }
}
}
