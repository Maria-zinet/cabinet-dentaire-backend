

package com.cabinetdentaire.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabinetdentaire.entities.RendezVous;
import com.cabinetdentaire.services.RendezVousService;

@CrossOrigin(
    origins = "http://localhost:4200",
    methods = {
        org.springframework.web.bind.annotation.RequestMethod.GET,
        org.springframework.web.bind.annotation.RequestMethod.POST,
        org.springframework.web.bind.annotation.RequestMethod.PUT,
        org.springframework.web.bind.annotation.RequestMethod.PATCH,
        org.springframework.web.bind.annotation.RequestMethod.DELETE
    }
)
@RestController
@RequestMapping("/api/rendezvous")
public class RendezVousController {

    private final RendezVousService rendezVousService;

    public RendezVousController(
            RendezVousService rendezVousService) {

        this.rendezVousService = rendezVousService;
    }

    // ==================================
    // TOUS LES RENDEZ-VOUS
    // ==================================

    @GetMapping
    public List<RendezVous> getAllRendezVous() {

        return rendezVousService.getAllRendezVous();
    }

    // ==================================
    // RENDEZ-VOUS PAR ID
    // ==================================

    @GetMapping("/{id}")
    public RendezVous getRendezVousById(
            @PathVariable Long id) {

        return rendezVousService.getRendezVousById(id);
    }

    // ==================================
    // CREER UN RENDEZ-VOUS
    // ==================================

    @PostMapping
    public RendezVous createRendezVous(
            @RequestBody RendezVous rdv) {

        return rendezVousService.createRendezVous(rdv);
    }

    // ==================================
    // MODIFIER UN RENDEZ-VOUS
    // ==================================

    @PutMapping("/{id}")
    public RendezVous updateRendezVous(
            @PathVariable Long id,
            @RequestBody RendezVous rdv) {

        return rendezVousService.updateRendezVous(id, rdv);
    }

    // ==================================
    // SUPPRIMER UN RENDEZ-VOUS
    // ==================================

    @DeleteMapping("/{id}")
    public void deleteRendezVous(
            @PathVariable Long id) {

        rendezVousService.deleteRendezVous(id);
    }

    // ==================================
    // MES RENDEZ-VOUS (PATIENT)
    // ==================================

    @GetMapping("/patient/{patientId}")
    public List<RendezVous> getRendezVousPatient(
            @PathVariable Long patientId) {

        return rendezVousService
                .getRendezVousPatient(patientId);
    }

    // ==================================
    // CONFIRMER UNE RESERVATION
    // ==================================

    @PostMapping("/reserver")
    public RendezVous reserver(
            @RequestBody ReservationRequest demande) {

        return rendezVousService.reserver(
                demande.patientId(),
                demande.disponibiliteId()
        );
    }

    // ==================================
    // ANNULER UNE RESERVATION
    // ==================================

    @PatchMapping("/{id}/annuler")
    public RendezVous annuler(
            @PathVariable Long id,
            @RequestBody PatientRequest demande) {

        return rendezVousService.annuler(
                id,
                demande.patientId()
        );
    }

    // ==================================
    // REPLANIFIER UNE RESERVATION
    // ==================================

    @PatchMapping("/{id}/replanifier")
    public RendezVous replanifier(
            @PathVariable Long id,
            @RequestBody ReservationRequest demande) {

        return rendezVousService.replanifier(
                id,
                demande.patientId(),
                demande.disponibiliteId()
        );
    }

    // ==================================
    // DONNEES RECUES D'ANGULAR
    // ==================================

    public record ReservationRequest(
            Long patientId,
            Long disponibiliteId
    ) {}

    public record PatientRequest(
            Long patientId
    ) {}
}
