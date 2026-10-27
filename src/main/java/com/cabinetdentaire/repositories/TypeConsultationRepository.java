package com.cabinetdentaire.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cabinetdentaire.entities.TypeConsultation;

public interface TypeConsultationRepository 
        extends JpaRepository<TypeConsultation, Long> {

}