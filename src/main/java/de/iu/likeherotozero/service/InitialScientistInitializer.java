/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.iu.likeherotozero.service;

import de.iu.likeherotozero.dao.ScientistDao;
import de.iu.likeherotozero.model.Scientist;
import de.iu.likeherotozero.security.PasswordService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
/**
 *
 * @author Iskender Dumlu
 */
@ApplicationScoped
public class InitialScientistInitializer {

    @Inject
    private ScientistDao scientistDao;

    @Inject
    private PasswordService passwordService;

    private boolean initializationChecked;

    public synchronized void initializeIfNecessary() {
        if (initializationChecked) {
            return;
        }

        String fullName
                = System.getenv("LHTZ_SCIENTIST_NAME");
        String email
                = System.getenv("LHTZ_SCIENTIST_EMAIL");
        String password
                = System.getenv("LHTZ_SCIENTIST_PASSWORD");

        if (isBlank(fullName)
                || isBlank(email)
                || isBlank(password)) {
            throw new IllegalStateException(
                    "Die Zugangsdaten des Wissenschaftlers fehlen."
            );
        }

        Scientist existingScientist
                = scientistDao.findByEmail(email);

        if (existingScientist == null) {
            Scientist scientist = new Scientist();
            scientist.setFullName(fullName);
            scientist.setEmail(email);
            scientist.setPasswordHash(
                    passwordService.hash(password)
            );

            scientistDao.save(scientist);
        }

        initializationChecked = true;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
