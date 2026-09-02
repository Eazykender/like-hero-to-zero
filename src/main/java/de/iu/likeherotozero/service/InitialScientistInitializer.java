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

    private static final String DEMO_NAME = "Tester";
    private static final String DEMO_EMAIL = "tester@iu.de";

    private static final String DEMO_PASSWORD_HASH
            = "65536:yqgYFijG31BPKIr6czKGXw==:"
            + "6BZqXuQF1xMSqVj1VL2pwfEetOWf2a7UJbrtlxUAqJo=";

    @Inject
    private ScientistDao scientistDao;

    @Inject
    private PasswordService passwordService;

    private boolean initializationChecked;

    public synchronized void initializeIfNecessary() {
        if (initializationChecked) {
            return;
        }

        initializePersonalScientistIfConfigured();
        initializeDemoScientist();

        initializationChecked = true;
    }

    private void initializePersonalScientistIfConfigured() {
        String fullName
                = System.getenv("LHTZ_SCIENTIST_NAME");
        String email
                = System.getenv("LHTZ_SCIENTIST_EMAIL");
        String password
                = System.getenv("LHTZ_SCIENTIST_PASSWORD");

        if (isBlank(fullName)
                || isBlank(email)
                || isBlank(password)) {
            return;
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
    }

    private void initializeDemoScientist() {
        Scientist demoScientist
                = scientistDao.findByEmail(DEMO_EMAIL);

        if (demoScientist == null) {
            demoScientist = new Scientist();
            demoScientist.setEmail(DEMO_EMAIL);
        }

        demoScientist.setFullName(DEMO_NAME);
        demoScientist.setPasswordHash(DEMO_PASSWORD_HASH);

        scientistDao.save(demoScientist);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
