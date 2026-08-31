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
}
