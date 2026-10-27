package com.cabinetdentaire.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cabinetdentaire.entities.Role;
import com.cabinetdentaire.repositories.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    // Injection du Repository
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Récupérer tous les rôles
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Récupérer un rôle par son ID
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow();
    }

    // Ajouter un rôle
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    // Modifier un rôle
    public Role updateRole(Long id, Role nouveauRole) {

        Role role = roleRepository.findById(id).orElseThrow();

        role.setLibelle(nouveauRole.getLibelle());

        return roleRepository.save(role);
    }

    // Supprimer un rôle
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}