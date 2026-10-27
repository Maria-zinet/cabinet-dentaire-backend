
package com.cabinetdentaire.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.cabinetdentaire.entities.Disponibilite;
import com.cabinetdentaire.services.DisponibiliteService;

@RestController
@RequestMapping("/api/disponibilites")
@CrossOrigin(origins = "http://localhost:4200")
public class DisponibiliteController {

    private final DisponibiliteService disponibiliteService;

    public DisponibiliteController(
            DisponibiliteService disponibiliteService) {

        this.disponibiliteService = disponibiliteService;
    }

    // GET : toutes les disponibilités futures
    @GetMapping
    public List<Disponibilite> getAll() {

        return disponibiliteService.getAllDisponibilites();
    }

    // GET : disponibilités par date
    // /api/disponibilites/date/2026-10-20
    @GetMapping("/date/{date}")
    public List<Disponibilite> getByDate(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        return disponibiliteService.getByDate(date);
    }

    // GET : disponibilités par consultation
    // /api/disponibilites/type/1
    @GetMapping("/type/{typeId}")
    public List<Disponibilite> getByType(
            @PathVariable Long typeId) {

        return disponibiliteService.getByTypeConsultation(typeId);
    }

    // GET : disponibilité par ID
    @GetMapping("/{id}")
    public Disponibilite getById(
            @PathVariable Long id) {

        return disponibiliteService.getById(id);
    }

    // POST : publier une disponibilité
    @PostMapping
    public Disponibilite create(
            @RequestBody Disponibilite disponibilite) {

        return disponibiliteService
                .createDisponibilite(disponibilite);
    }

    // DELETE : supprimer une disponibilité
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {

        disponibiliteService.deleteDisponibilite(id);
    }
}
