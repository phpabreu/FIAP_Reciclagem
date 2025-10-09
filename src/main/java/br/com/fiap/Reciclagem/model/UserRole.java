package br.com.fiap.Reciclagem.model;

public enum UserRole {

    ADMIN("Admin"), USER("User");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

}