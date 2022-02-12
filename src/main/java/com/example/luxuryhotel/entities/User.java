package com.example.luxuryhotel.entities;


import javax.persistence.*;
import java.util.Set;

@Entity(name = "usr")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String username;
    @Column (unique = true)
    private String email;
    private boolean active;
    String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;


    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public User setId(Integer id) {this.id = id; return this;}

    public String getUsername() {
        return username;
    }

    public User setUsername(String name) {this.username = name; return this;}

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {this.email = email; return this;}

    public boolean isActive() {
        return active;
    }

    public User setActive(boolean active) {this.active = active; return  this;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public User setRoles(Set<Role> roles) {this.roles = roles; return this;}
}