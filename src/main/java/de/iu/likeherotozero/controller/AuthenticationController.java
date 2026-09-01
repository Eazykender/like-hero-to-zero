/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.iu.likeherotozero.controller;
import de.iu.likeherotozero.dao.ScientistDao;
import de.iu.likeherotozero.model.Scientist;
import de.iu.likeherotozero.security.PasswordService;
import de.iu.likeherotozero.service.InitialScientistInitializer;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
/**
 *
 * @author Iskender Dumlu
 */
@Named
@SessionScoped
public class AuthenticationController implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private ScientistDao scientistDao;

    @Inject
    private PasswordService passwordService;

    @Inject
    private InitialScientistInitializer scientistInitializer;

    private String email;
    private String password;
    private Long scientistId;
    private String scientistName;
    private String errorMessage;

    public String login() {
        scientistInitializer.initializeIfNecessary();

        Scientist scientist
                = scientistDao.findByEmail(email.trim());

        if (scientist != null
                && passwordService.matches(
                        password,
                        scientist.getPasswordHash())) {

            scientistId = scientist.getId();
            scientistName = scientist.getFullName();
            password = null;
            errorMessage = null;

            return "backend?faces-redirect=true";
        }

        password = null;
        errorMessage = "E-Mail-Adresse oder Passwort ist falsch.";
        return null;
    }
    
    public String checkLogin() {
        if (!isLoggedIn()) {
            return "login?faces-redirect=true";
        }
        return null;
    }
    
    
    public String logout() {
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .invalidateSession();

        return "index?faces-redirect=true";
    }

    public boolean isLoggedIn() {
        return scientistId != null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getScientistId() {
        return scientistId;
    }

    public String getScientistName() {
        return scientistName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

