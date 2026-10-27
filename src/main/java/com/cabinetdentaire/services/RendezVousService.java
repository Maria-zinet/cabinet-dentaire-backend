
package com.cabinetdentaire.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.cabinetdentaire.entities.Disponibilite;
import com.cabinetdentaire.entities.RendezVous;
import com.cabinetdentaire.entities.StatutRendezVous;
import com.cabinetdentaire.entities.TypeConsultation;
import com.cabinetdentaire.entities.User;

import com.cabinetdentaire.repositories.DisponibiliteRepository;
import com.cabinetdentaire.repositories.RendezVousRepository;
import com.cabinetdentaire.repositories.TypeConsultationRepository;
import com.cabinetdentaire.repositories.UserRepository;

@Service
public class RendezVousService {

    private final RendezVousRepository rendezVousRepository;
    private final DisponibiliteRepository disponibiliteRepository;
    private final UserRepository userRepository;
    private final TypeConsultationRepository typeConsultationRepository;

    public RendezVousService(
            RendezVousRepository rendezVousRepository,
            DisponibiliteRepository disponibiliteRepository,
            UserRepository userRepository,
            TypeConsultationRepository typeConsultationRepository) {

        this.rendezVousRepository = rendezVousRepository;
        this.disponibiliteRepository = disponibiliteRepository;
        this.userRepository = userRepository;
        this.typeConsultationRepository = typeConsultationRepository;
    }

    // ====================================
    // CRUD EXISTANT
    // ====================================

    public List<RendezVous> getAllRendezVous() {
        return rendezVousRepository.findAll();
    }

    public RendezVous getRendezVousById(Long id) {
        return rendezVousRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Rendez-vous introuvable"
                ));
    }

    public List<RendezVous> getRendezVousPatient(Long patientId) {
        return rendezVousRepository
                .findByPatientIdOrderByDateDescHeureDesc(patientId);
    }

    @Transactional
    public RendezVous createRendezVous(RendezVous nouveau) {

        if (nouveau.getPatient() == null
                || nouveau.getPatient().getId() == null
                || nouveau.getTypeConsultation() == null
                || nouveau.getTypeConsultation().getId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Patient et type de consultation obligatoires"
            );
        }

        User patient = chercherPatient(
                nouveau.getPatient().getId()
        );

        TypeConsultation type = typeConsultationRepository
                .findById(nouveau.getTypeConsultation().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Consultation introuvable"
                ));

        nouveau.setId(null);
        nouveau.setPatient(patient);
        nouveau.setTypeConsultation(type);

        return rendezVousRepository.save(nouveau);
    }

    @Transactional
    public RendezVous updateRendezVous(
            Long id,
            RendezVous nouveau) {

        RendezVous rdv = getRendezVousById(id);

        rdv.setDate(nouveau.getDate());
        rdv.setHeure(nouveau.getHeure());
        rdv.setMotif(nouveau.getMotif());

        rdv.setPremiereConsultation(
                nouveau.getPremiereConsultation()
        );

        rdv.setStatut(nouveau.getStatut());

        if (nouveau.getPatient() != null
                && nouveau.getPatient().getId() != null) {

            rdv.setPatient(
                    chercherPatient(nouveau.getPatient().getId())
            );
        }

        if (nouveau.getTypeConsultation() != null
                && nouveau.getTypeConsultation().getId() != null) {

            TypeConsultation type = typeConsultationRepository
                    .findById(nouveau.getTypeConsultation().getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Consultation introuvable"
                    ));

            rdv.setTypeConsultation(type);
        }

        return rendezVousRepository.save(rdv);
    }

    @Transactional
    public void deleteRendezVous(Long id) {
        rendezVousRepository.deleteById(id);
    }

    // ====================================
    // RESERVER UN CRENEAU
    // ====================================

    @Transactional
    public RendezVous reserver(
            Long patientId,
            Long disponibiliteId) {

        if (patientId == null || disponibiliteId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Patient et disponibilité obligatoires"
            );
        }

        // Verrouiller le créneau choisi
        Disponibilite dispo = disponibiliteRepository
                .findByIdForUpdate(disponibiliteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Ce créneau n'est plus disponible"
                ));

        verifierDateFuture(dispo);

        // Empêcher un RDV déjà réservé au même horaire
        verifierHoraireLibre(dispo);

        User patient = chercherPatient(patientId);

        RendezVous rdv = new RendezVous();

        rdv.setDate(dispo.getDate());
        rdv.setHeure(dispo.getHeure());
        rdv.setPatient(patient);
        rdv.setTypeConsultation(dispo.getTypeConsultation());
        rdv.setStatut(StatutRendezVous.CONFIRME);

        rdv.setMotif(null);
        rdv.setPremiereConsultation(false);

        // Créer le RDV
        RendezVous resultat = rendezVousRepository.save(rdv);

        // Retirer l'horaire de la liste des créneaux libres
        disponibiliteRepository.delete(dispo);

        return resultat;
    }

    // ====================================
    // ANNULER UN RENDEZ-VOUS
    // ====================================

    @Transactional
    public RendezVous annuler(
            Long rendezVousId,
            Long patientId) {

        RendezVous rdv = chercherRendezVousVerrouille(
                rendezVousId,
                patientId
        );

        if (rdv.getStatut() == StatutRendezVous.ANNULE) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce rendez-vous est déjà annulé"
            );
        }

        if (rdv.getStatut() == StatutRendezVous.PRESENT
                || rdv.getStatut() == StatutRendezVous.ABSENT) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce rendez-vous ne peut plus être annulé"
            );
        }

        verifierDateFuture(rdv);

        rdv.setStatut(StatutRendezVous.ANNULE);

        RendezVous resultat = rendezVousRepository.save(rdv);

        // Remettre l'ancien horaire à disposition
        libererHoraire(
                rdv.getDate(),
                rdv.getHeure(),
                rdv.getTypeConsultation()
        );

        return resultat;
    }

    // ====================================
    // REPLANIFIER UN RENDEZ-VOUS
    // ====================================

    @Transactional
    public RendezVous replanifier(
            Long rendezVousId,
            Long patientId,
            Long nouvelleDisponibiliteId) {

        RendezVous rdv = chercherRendezVousVerrouille(
                rendezVousId,
                patientId
        );

        if (rdv.getStatut() == StatutRendezVous.ANNULE
                || rdv.getStatut() == StatutRendezVous.PRESENT
                || rdv.getStatut() == StatutRendezVous.ABSENT) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce rendez-vous ne peut pas être déplacé"
            );
        }

        verifierDateFuture(rdv);

        Disponibilite nouvelleDispo = disponibiliteRepository
                .findByIdForUpdate(nouvelleDisponibiliteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Le nouvel horaire n'est plus disponible"
                ));

        verifierDateFuture(nouvelleDispo);
        verifierHoraireLibre(nouvelleDispo);

        LocalDate ancienneDate = rdv.getDate();
        java.time.LocalTime ancienneHeure = rdv.getHeure();
        TypeConsultation ancienType = rdv.getTypeConsultation();

        // Affecter le nouvel horaire
        rdv.setDate(nouvelleDispo.getDate());
        rdv.setHeure(nouvelleDispo.getHeure());
        rdv.setTypeConsultation(
                nouvelleDispo.getTypeConsultation()
        );

        rdv.setStatut(StatutRendezVous.CONFIRME);

        RendezVous resultat = rendezVousRepository.save(rdv);

        // Consommer le nouvel horaire
        disponibiliteRepository.delete(nouvelleDispo);

        // Libérer l'ancien horaire
        libererHoraire(
                ancienneDate,
                ancienneHeure,
                ancienType
        );

        return resultat;
    }

    // ====================================
    // METHODES DE VERIFICATION
    // ====================================

    private User chercherPatient(Long patientId) {

        if (patientId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Patient obligatoire"
            );
        }

        return userRepository.findById(patientId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Patient introuvable"
                ));
    }

    private RendezVous chercherRendezVousVerrouille(
            Long rendezVousId,
            Long patientId) {

        RendezVous rdv = rendezVousRepository
                .findByIdForUpdate(rendezVousId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Rendez-vous introuvable"
                ));

        if (rdv.getPatient() == null
                || !rdv.getPatient().getId().equals(patientId)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Ce rendez-vous n'appartient pas à ce patient"
            );
        }

        return rdv;
    }

    private void verifierDateFuture(Disponibilite dispo) {

        LocalDateTime dateHeure = LocalDateTime.of(
                dispo.getDate(),
                dispo.getHeure()
        );

        if (!dateHeure.isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le créneau doit être dans le futur"
            );
        }
    }

    private void verifierDateFuture(RendezVous rdv) {

        LocalDateTime dateHeure = LocalDateTime.of(
                rdv.getDate(),
                rdv.getHeure()
        );

        if (!dateHeure.isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce rendez-vous est déjà passé"
            );
        }
    }

    private void verifierHoraireLibre(Disponibilite dispo) {

        boolean occupe =
                rendezVousRepository.existsByDateAndHeureAndStatutNot(
                        dispo.getDate(),
                        dispo.getHeure(),
                        StatutRendezVous.ANNULE
                );

        if (occupe) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cet horaire est déjà réservé"
            );
        }
    }

    private void libererHoraire(
            LocalDate date,
            java.time.LocalTime heure,
            TypeConsultation type) {

        if (!LocalDateTime.of(date, heure)
                .isAfter(LocalDateTime.now())) {
            return;
        }

        boolean existe =
                disponibiliteRepository.existsByDateAndHeure(
                        date,
                        heure
                );

        if (!existe) {

            Disponibilite nouvelle = new Disponibilite();

            nouvelle.setDate(date);
            nouvelle.setHeure(heure);
            nouvelle.setTypeConsultation(type);

            disponibiliteRepository.save(nouvelle);
        }
    }
}
