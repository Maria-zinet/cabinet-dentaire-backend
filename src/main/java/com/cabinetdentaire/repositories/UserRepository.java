package com.cabinetdentaire.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cabinetdentaire.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	//NV//
	User findByEmail(String email);
}
