package com.example.backend.model;

import java.util.Objects;

public class User {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;

    private String loginEmail;
    private String loginPassword;

    public User() {

    }

    public User(Long id, String name, String surname, String email, String password, String loginEmail,
	    String loginPassword) {
	this.id = id;
	this.name = name;
	this.surname = surname;
	this.email = email;
	this.password = password;
	this.loginEmail = loginEmail;
	this.loginPassword = loginPassword;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getSurname() {
	return surname;
    }

    public void setSurname(String surname) {
	this.surname = surname;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getLoginEmail() {
	return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
	this.loginEmail = loginEmail;
    }

    public String getLoginPassword() {
	return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
	this.loginPassword = loginPassword;
    }

    @Override
    public String toString() {
     return "User{" + 
	     "id=" +id + 
	     ", name='" + name + '\'' + 
	     ", surname='" + surname + '\'' + 
	     ", email='" + email + '\'' + 
	     ", password='" +password + '\'' + "}";
    }
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null || getClass() != obj.getClass())
	    return false;
	User user = (User) obj;
	return Objects.equals(id, user.id) 
		&& Objects.equals(name, user.name)  
		&& Objects.equals(surname, user.surname)  
		&& Objects.equals(email, user.email)  
		&& Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
	return Objects.hash(id, name, surname, email, password);
    }
}
