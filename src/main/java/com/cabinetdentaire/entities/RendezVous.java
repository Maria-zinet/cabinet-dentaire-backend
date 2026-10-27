package com.cabinetdentaire.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "rendez_vous")
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private LocalTime heure;

    private String motif;

    private Boolean premiereConsultation;

    @Enumerated(EnumType.STRING)
    private StatutRendezVous statut;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient;

    @ManyToOne
    @JoinColumn(name = "type_consultation_id")
    private TypeConsultation typeConsultation;


    public RendezVous() {
    }


    public RendezVous(
            LocalDate date,
            LocalTime heure,
            String motif,
            Boolean premiereConsultation,
            StatutRendezVous statut,
            User patient,
            TypeConsultation typeConsultation) {

        this.date = date;
        this.heure = heure;
        this.motif = motif;
        this.premiereConsultation = premiereConsultation;
        this.statut = statut;
        this.patient = patient;
        this.typeConsultation = typeConsultation;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public LocalDate getDate() {
        return date;
    }


    public void setDate(LocalDate date) {
        this.date = date;
    }


    public LocalTime getHeure() {
        return heure;
    }


    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }


    public String getMotif() {
        return motif;
    }


    public void setMotif(String motif) {
        this.motif = motif;
    }


    public Boolean getPremiereConsultation() {
        return premiereConsultation;
    }


    public void setPremiereConsultation(Boolean premiereConsultation) {
        this.premiereConsultation = premiereConsultation;
    }


    public StatutRendezVous getStatut() {
        return statut;
    }


    public void setStatut(StatutRendezVous statut) {
        this.statut = statut;
    }


    public User getPatient() {
        return patient;
    }


    public void setPatient(User patient) {
        this.patient = patient;
    }


    public TypeConsultation getTypeConsultation() {
        return typeConsultation;
    }


    public void setTypeConsultation(TypeConsultation typeConsultation) {
        this.typeConsultation = typeConsultation;
    }
}