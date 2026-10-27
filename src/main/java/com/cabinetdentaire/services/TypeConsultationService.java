package com.cabinetdentaire.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cabinetdentaire.entities.TypeConsultation;
import com.cabinetdentaire.repositories.TypeConsultationRepository;

@Service
public class TypeConsultationService {

    private final TypeConsultationRepository typeConsultationRepository;

    public TypeConsultationService(
            TypeConsultationRepository typeConsultationRepository) {

        this.typeConsultationRepository = typeConsultationRepository;
    }

    // Récupérer tous les types de consultation
    public List<TypeConsultation> getAllTypes() {
        return typeConsultationRepository.findAll();
    }

    // Récupérer un type par son ID
    public TypeConsultation getTypeById(Long id) {
        return typeConsultationRepository.findById(id)
                .orElseThrow();
    }

    // Ajouter un type de consultation
    public TypeConsultation createType(TypeConsultation typeConsultation) {
        return typeConsultationRepository.save(typeConsultation);
    }

    // Modifier un type de consultation
    public TypeConsultation updateType(
            Long id,
            TypeConsultation nouveauType) {

        TypeConsultation type = typeConsultationRepository.findById(id)
                .orElseThrow();

        type.setNom(nouveauType.getNom());
        type.setDuree(nouveauType.getDuree());

        return typeConsultationRepository.save(type);
    }

    // Supprimer un type de consultation
    public void deleteType(Long id) {
        typeConsultationRepository.deleteById(id);
    }
}