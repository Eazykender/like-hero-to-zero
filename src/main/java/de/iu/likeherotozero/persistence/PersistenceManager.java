/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.iu.likeherotozero.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
/**
 *
 * @author Iskender Dumlu
 */
@ApplicationScoped
public class PersistenceManager {
    
    private EntityManagerFactory entityManagerFactory;

public void initialize(
        @Observes @Initialized(ApplicationScoped.class) Object event) {
    String databaseUser = System.getenv("LHTZ_DB_USER");
    String databasePassword = System.getenv("LHTZ_DB_PASSWORD");

    if (databaseUser == null || databasePassword == null) {
        throw new IllegalStateException(
                "Die lokalen Datenbank-Zugangsdaten fehlen.");
    }

    Map<String, Object> properties = new HashMap<>();
    properties.put("jakarta.persistence.jdbc.user", databaseUser);
    properties.put("jakarta.persistence.jdbc.password", databasePassword);

    entityManagerFactory = Persistence.createEntityManagerFactory(
            "likeHeroToZeroPU", properties);
}
public EntityManager createEntityManager() {
    if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
        throw new IllegalStateException(
                "Die JPA-Verbindung ist nicht verfügbar.");
    }

    return entityManagerFactory.createEntityManager();
}

@PreDestroy
public void close() {
    if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
        entityManagerFactory.close();
    }

    AbandonedConnectionCleanupThread.checkedShutdown();
}  
}
