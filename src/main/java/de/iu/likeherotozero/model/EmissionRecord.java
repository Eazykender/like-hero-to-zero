/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.iu.likeherotozero.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import java.math.BigDecimal;
import jakarta.persistence.UniqueConstraint;
/**
 *
 * @author Iskender Dumlu
 */
@Entity
@Table(
        name = "emission_records",
        uniqueConstraints = @UniqueConstraint(
        columnNames = {"country_id", "emission_year"}
    )
)
public class EmissionRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;
    
    @Column(name = "emission_year", nullable = false)
    private Integer year;
    
    @Column(name = "co2_emissions_kt", nullable = false, precision = 19, scale = 3)
    private BigDecimal co2EmissionsKt;
    
        
    @ManyToOne
    @JoinColumn(name = "last_modified_by_id")
    private Scientist lastModifiedBy;

    public Scientist getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Scientist lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
          
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
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

    public void setCo2EmissionsKt(BigDecimal co2EmissionsKt) {
        this.co2EmissionsKt = co2EmissionsKt;
    }
    
}
