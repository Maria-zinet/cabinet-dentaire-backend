package com.cabinetdentaire.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cabinetdentaire.entities.Role;
import com.cabinetdentaire.entities.User;
import com.cabinetdentaire.repositories.RoleRepository;
import com.cabinetdentaire.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // Récupérer tous les utilisateurs
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Récupérer un utilisateur par son ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow();
    }

    // Ajouter un utilisateur
    public User createUser(User user) {

        if (user.getRole() != null
                && user.getRole().getId() != null) {

            Long roleId = user.getRole().getId();

            Role role = roleRepository
                    .findById(roleId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Role introuvable : " + roleId
                            )
                    );

            user.setRole(role);
        }

        return userRepository.save(user);
    }
    //nv//
    public User login(String email, String password) {

        User user = userRepository.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }

    // Modifier un utilisateur
    public User updateUser(Long id, User nouveauUser) {

        User user = userRepository.findById(id)
                .orElseThrow();

        user.setNom(nouveauUser.getNom());
        user.setPrenom(nouveauUser.getPrenom());
        user.setDateNaissance(nouveauUser.getDateNaissance());
        user.setTelephone(nouveauUser.getTelephone());
        user.setEmail(nouveauUser.getEmail());
        user.setPassword(nouveauUser.getPassword());

        // Modifier le rôle
        if (nouveauUser.getRole() != null
                && nouveauUser.getRole().getId() != null) {

            Long roleId = nouveauUser.getRole().getId();

            Role role = roleRepository.findById(roleId)
                    .orElseThrow();

            user.setRole(role);
        }

        return userRepository.save(user);
    }

    // Supprimer un utilisateur
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}