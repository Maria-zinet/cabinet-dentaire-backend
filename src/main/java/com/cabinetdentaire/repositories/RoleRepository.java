package com.cabinetdentaire.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cabinetdentaire.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByLibelle(String libelle);

}