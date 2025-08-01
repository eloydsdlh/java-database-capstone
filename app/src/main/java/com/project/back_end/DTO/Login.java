package com.project.back_end.DTO;

public class Login {
    
    private String identifier;

    private String password;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String email) {
        this.identifier = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
