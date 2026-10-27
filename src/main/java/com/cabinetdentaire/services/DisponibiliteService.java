
package com.cabinetdentaire.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cabinetdentaire.entities.Disponibilite;
import com.cabinetdentaire.entities.TypeConsultation;

import com.cabinetdentaire.repositories.DisponibiliteRepository;
import com.cabinetdentaire.repositories.TypeConsultationRepository;

@Service
public class DisponibiliteService {

    private final DisponibiliteRepository disponibiliteRepository;

    private final TypeConsultationRepository typeConsultationRepository;

    public DisponibiliteService(
            DisponibiliteRepository disponibiliteRepository,
            TypeConsultationRepository typeConsultationRepository) {

        this.disponibiliteRepository = disponibiliteRepository;
        this.typeConsultationRepository = typeConsultationRepository;
    }

    // Récupérer toutes les disponibilités futures
    public List<Disponibilite> getAllDisponibilites() {

        return disponibiliteRepository
                .findByDateGreaterThanEqualOrderByDateAscHeureAsc(
                        LocalDate.now()
                );
    }

    // Récupérer les disponibilités d'une date
    public List<Disponibilite> getByDate(LocalDate date) {

        return disponibiliteRepository.findByDate(date);
    }

    // Récupérer par type de consultation
    public List<Disponibilite> getByTypeConsultation(Long typeId) {

        return disponibiliteRepository
                .findByTypeConsultationId(typeId);
    }

    // Récupérer une disponibilité par son ID
    public Disponibilite getById(Long id) {

        return disponibiliteRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Disponibilité introuvable"
                        )
                );
    }

    // PUBLIER UNE DISPONIBILITÉ
    public Disponibilite createDisponibilite(
            Disponibilite disponibilite) {

        // Vérification des champs
        if (disponibilite.getDate() == null
                || disponibilite.getHeure() == null
                || disponibilite.getTypeConsultation() == null
                || disponibilite.getTypeConsultation().getId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Date, heure et consultation obligatoires"
            );
        }

        LocalDate date = disponibilite.getDate();
        LocalTime heure = disponibilite.getHeure();

        // Interdire les créneaux passés
        LocalDateTime dateHeure = LocalDateTime.of(date, heure);

        if (!dateHeure.isAfter(LocalDateTime.now())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Choisissez une date et une heure futures"
            );
        }

        // Empêcher un créneau identique
        if (disponibiliteRepository.existsByDateAndHeure(
                date, heure)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Un créneau existe déjà à cette date et heure"
            );
        }

        // Vérifier que le type de consultation existe
        Long typeId = disponibilite
                .getTypeConsultation()
                .getId();

        TypeConsultation consultation =
                typeConsultationRepository.findById(typeId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Type de consultation introuvable"
                                )
                        );

        disponibilite.setId(null);
        disponibilite.setTypeConsultation(consultation);

        // Enregistrer dans MySQL
        return disponibiliteRepository.save(disponibilite);
    }

    // SUPPRIMER UNE DISPONIBILITÉ
    public void deleteDisponibilite(Long id) {

        if (!disponibiliteRepository.existsById(id)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Disponibilité introuvable"
            );
        }

        disponibiliteRepository.deleteById(id);
    }
}
