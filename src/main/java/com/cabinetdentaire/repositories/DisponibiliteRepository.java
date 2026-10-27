
package com.cabinetdentaire.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cabinetdentaire.entities.RendezVous;
import com.cabinetdentaire.entities.StatutRendezVous;

public interface RendezVousRepository
        extends JpaRepository<RendezVous, Long> {

    // Rendez-vous d'un patient
    List<RendezVous> findByPatientIdOrderByDateDescHeureDesc(
            Long patientId
    );

    // Vérifier si un rendez-vous actif existe
    boolean existsByDateAndHeureAndStatutNot(
            LocalDate date,
            LocalTime heure,
            StatutRendezVous statut
    );

    // Verrouiller un rendez-vous dans MariaDB
    @Query(
        value = """
            SELECT *
            FROM rendez_vous
            WHERE id = :id
            FOR UPDATE
            """,
        nativeQuery = true
    )
    Optional<RendezVous> findByIdForUpdate(
            @Param("id") Long id
    );
}

