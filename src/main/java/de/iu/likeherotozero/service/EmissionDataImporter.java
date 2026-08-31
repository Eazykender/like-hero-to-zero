/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.iu.likeherotozero.service;

import de.iu.likeherotozero.model.Country;
import de.iu.likeherotozero.model.EmissionRecord;
import de.iu.likeherotozero.persistence.PersistenceManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Iskender Dumlu
 */
@ApplicationScoped
public class EmissionDataImporter {
    
    private static final String DATA_FILE
            = "/data/annual-co2-emissions-per-country.csv";

    @Inject
    private PersistenceManager persistenceManager;

    private boolean importChecked;

    public synchronized void importIfNecessary() {
        if (importChecked) {
            return;
        }

        EntityManager entityManager
                = persistenceManager.createEntityManager();

        try {
            Long recordCount = entityManager.createQuery(
                    "SELECT COUNT(e) FROM EmissionRecord e",
                    Long.class
            ).getSingleResult();

            if (recordCount == 0) {
                importLatestRecords(entityManager);
            }

            importChecked = true;
        } finally {
            entityManager.close();
        }
    }

    private void importLatestRecords(EntityManager entityManager) {
        Map<String, CsvRecord> latestRecords
                = readLatestRecords();

        EntityTransaction transaction
                = entityManager.getTransaction();

        try {
            transaction.begin();

            for (CsvRecord csvRecord : latestRecords.values()) {
                Country country = new Country();
                country.setName(csvRecord.countryName);
                country.setIsoCode(csvRecord.isoCode);
                entityManager.persist(country);

                EmissionRecord emissionRecord
                        = new EmissionRecord();

                emissionRecord.setCountry(country);
                emissionRecord.setYear(csvRecord.year);
                emissionRecord.setCo2EmissionsKt(
                        csvRecord.emissionsTonnes
                                .divide(
                                        new BigDecimal("1000"),
                                        3,
                                        RoundingMode.HALF_UP
                                )
                );

                entityManager.persist(emissionRecord);
            }

            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        }
    }

    private Map<String, CsvRecord> readLatestRecords() {
        Map<String, CsvRecord> latestRecords
                = new LinkedHashMap<>();

        InputStream inputStream
                = getClass().getResourceAsStream(DATA_FILE);

        if (inputStream == null) {
            throw new IllegalStateException(
                    "Die CO2-Datendatei wurde nicht gefunden.");
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        inputStream,
                        StandardCharsets.UTF_8
                ))) {

            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                List<String> values = parseCsvLine(line);

                if (values.size() != 4) {
                    continue;
                }

                String countryName = values.get(0);
                String isoCode = values.get(1);
                int year = Integer.parseInt(values.get(2));
                BigDecimal emissionsTonnes
                        = new BigDecimal(values.get(3));

                if (!isoCode.matches("[A-Z]{3}")) {
                    continue;
                }

                CsvRecord existingRecord
                        = latestRecords.get(isoCode);

                if (existingRecord == null
                        || year > existingRecord.year) {
                    latestRecords.put(
                            isoCode,
                            new CsvRecord(
                                    countryName,
                                    isoCode,
                                    year,
                                    emissionsTonnes
                            )
                    );
                }
            }
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Die CO2-Datendatei konnte nicht gelesen werden.",
                    exception
            );
        }

        return latestRecords;
    }

    private List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean insideQuotes = false;

        for (int index = 0; index < line.length(); index++) {
            char character = line.charAt(index);

            if (character == '"') {
                if (insideQuotes
                        && index + 1 < line.length()
                        && line.charAt(index + 1) == '"') {
                    currentValue.append('"');
                    index++;
                } else {
                    insideQuotes = !insideQuotes;
                }
            } else if (character == ',' && !insideQuotes) {
                values.add(currentValue.toString());
                currentValue.setLength(0);
            } else {
                currentValue.append(character);
            }
        }

        values.add(currentValue.toString());
        return values;
    }

    private static class CsvRecord {

        private final String countryName;
        private final String isoCode;
        private final int year;
        private final BigDecimal emissionsTonnes;

        private CsvRecord(
                String countryName,
                String isoCode,
                int year,
                BigDecimal emissionsTonnes) {

            this.countryName = countryName;
            this.isoCode = isoCode;
            this.year = year;
            this.emissionsTonnes = emissionsTonnes;
        }
    }
}

