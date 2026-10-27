package com.cabinetdentaire.entities;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "users")
public class User {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;
private String nom;
private String prenom;
private LocalDate dateNaissance;
private String telephone;
private String password;
private String email;
// ✅ ICI : relation entre User et Role
@ManyToOne
@JoinColumn(name = "role_id")
private Role role;

public User() {
	super();
	// TODO Auto-generated constructor stub
}


public User(String nom, String prenom, LocalDate dateNaissance,
        String telephone, String email, String password, Role role) {

this.nom = nom;
this.prenom = prenom;
this.dateNaissance = dateNaissance;
this.telephone = telephone;
this.email = email;
this.password = password;
this.role = role;
}


public Long getId() {
	return id;
}


public void setId(Long id) {
	this.id = id;
}


public String getNom() {
	return nom;
}


public void setNom(String nom) {
	this.nom = nom;
}


public String getPrenom() {
	return prenom;
}


public void setPrenom(String prenom) {
	this.prenom = prenom;
}


public LocalDate getDateNaissance() {
	return dateNaissance;
}


public void setDateNaissance(LocalDate dateNaissance) {
	this.dateNaissance = dateNaissance;
}


public String getTelephone() {
	return telephone;
}


public void setTelephone(String telephone) {
	this.telephone = telephone;
}


public String getPassword() {
	return password;
}


public void setPassword(String password) {
	this.password = password;
}


public String getEmail() {
	return email;
}


public void setEmail(String email) {
	this.email = email;
}
public Role getRole() {
    return role;
}

public void setRole(Role role) {
    this.role = role;
}
}