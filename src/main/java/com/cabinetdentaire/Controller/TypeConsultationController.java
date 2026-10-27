package com.cabinetdentaire.Controller;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabinetdentaire.entities.TypeConsultation;
import com.cabinetdentaire.services.TypeConsultationService;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/types-consultation")

public class TypeConsultationController {

    private final TypeConsultationService typeConsultationService;

    public TypeConsultationController(
            TypeConsultationService typeConsultationService) {

        this.typeConsultationService = typeConsultationService;
    }

    // Récupérer tous les types
    @GetMapping
    public List<TypeConsultation> getAllTypes() {
        return typeConsultationService.getAllTypes();
    }

    // Récupérer un type par ID
    @GetMapping("/{id}")
    public TypeConsultation getTypeById(@PathVariable Long id) {
        return typeConsultationService.getTypeById(id);
    }

    // Ajouter un type
    @PostMapping
    public TypeConsultation createType(
            @RequestBody TypeConsultation typeConsultation) {

        return typeConsultationService.createType(typeConsultation);
    }

    // Modifier un type
    @PutMapping("/{id}")
    public TypeConsultation updateType(
            @PathVariable Long id,
            @RequestBody TypeConsultation typeConsultation) {

        return typeConsultationService.updateType(id, typeConsultation);
    }

    // Supprimer un type
    @DeleteMapping("/{id}")
    public void deleteType(@PathVariable Long id) {
        typeConsultationService.deleteType(id);
    }
}