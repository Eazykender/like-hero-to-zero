/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.iu.likeherotozero.controller;

import de.iu.likeherotozero.dao.CountryDao;
import de.iu.likeherotozero.dao.EmissionRecordDao;
import de.iu.likeherotozero.dao.ScientistDao;
import de.iu.likeherotozero.model.Country;
import de.iu.likeherotozero.model.EmissionRecord;
import de.iu.likeherotozero.model.Scientist;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.math.BigDecimal;
import java.util.List;
/**
 *
 * @author Iskender Dumlu
 */
@Named
@RequestScoped
public class BackendController {
    
    @Inject
    private CountryDao countryDao;

    @Inject
    private EmissionRecordDao emissionRecordDao;

    @Inject
    private ScientistDao scientistDao;

    @Inject
    private AuthenticationController authenticationController;

    private Long countryId;
    private Integer year;
    private BigDecimal co2EmissionsKt;
    private String message;

    public void save() {
        if (!authenticationController.isLoggedIn()) {
            message = "Bitte zuerst anmelden.";
            return;
        }

        Country country = countryDao.findById(countryId);
        Scientist scientist = scientistDao.findById(
                authenticationController.getScientistId()
        );

        if (country == null || scientist == null) {
            message = "Land oder Wissenschaftler wurde nicht gefunden.";
            return;
        }

        EmissionRecord emissionRecord
                = emissionRecordDao.findByCountryAndYear(
                        countryId,
                        year
                );

        boolean correction = emissionRecord != null;

        if (!correction) {
            emissionRecord = new EmissionRecord();
            emissionRecord.setCountry(country);
            emissionRecord.setYear(year);
        }

        emissionRecord.setCo2EmissionsKt(co2EmissionsKt);
        emissionRecord.setLastModifiedBy(scientist);

        emissionRecordDao.save(emissionRecord);

        if (correction) {
            message = "Der Emissionswert wurde korrigiert.";
        } else {
            message = "Der Emissionswert wurde hinzugefügt.";
        }

        co2EmissionsKt = null;
    }

    public List<Country> getCountries() {
        return countryDao.findAll();
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getCo2EmissionsKt() {
        return co2EmissionsKt;
    }

    public void setCo2EmissionsKt(
            BigDecimal co2EmissionsKt) {
        this.co2EmissionsKt = co2EmissionsKt;
    }

    public String getMessage() {
        return message;
    }
}

