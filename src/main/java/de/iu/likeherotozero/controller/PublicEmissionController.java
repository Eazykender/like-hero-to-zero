/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.iu.likeherotozero.controller;

import de.iu.likeherotozero.dao.EmissionRecordDao;
import de.iu.likeherotozero.model.EmissionRecord;
import de.iu.likeherotozero.service.EmissionDataImporter;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
/**
 *
 * @author Iskender Dumlu
 */
@Named
@RequestScoped
public class PublicEmissionController {

    private String projectTitle = "Like Hero To Zero";

    @Inject
    private EmissionRecordDao emissionRecordDao;

    @Inject
    private EmissionDataImporter emissionDataImporter;

    private List<EmissionRecord> latestEmissions;

    @PostConstruct
    public void initialize() {
        emissionDataImporter.importIfNecessary();

        latestEmissions
                = emissionRecordDao.findLatestForAllCountries();
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public List<EmissionRecord> getLatestEmissions() {
        return latestEmissions;
    }
}
