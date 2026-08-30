/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.iu.likeherotozero.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
/**
 *
 * @author Iskender Dumlu
 */
@Named
@RequestScoped
public class PublicEmissionController {
    
    private String projectTitle = "Like Hero To Zero";
    public String getProjectTitle() {
        return projectTitle;
    }
            
}
