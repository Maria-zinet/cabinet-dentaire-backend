
package com.cabinetdentaire.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cabinetdentaire.entities.RendezVous;
import com.cabinetdentaire.entities.StatutRendezVous;

public interface RendezVousRepository
        extends JpaRepository<RendezVous, Long> {

    List<RendezVous> findByPatientIdOrderByDateDescHeureDesc(
            Long patientId
    );

    // Vérifier qu'aucun RDV actif n'occupe l'horaire
    boolean existsByDateAndHeureAndStatutNot(
            LocalDate date,
            LocalTime heure,
            StatutRendezVous statut
    );

    // Verrouiller le RDV pendant annulation ou déplacement
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT r
        FROM RendezVous r
        WHERE r.id = :id
    """)
    Optional<RendezVous> findByIdForUpdate(
            @Param("id") Long id
    );
}
