package com.pksprojects.minis3.models.user;

import com.pksprojects.minis3.models.view.user.UserRegistrationView;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.pksprojects.minis3.models.user.Role.ADMIN;
import static com.pksprojects.minis3.models.user.Role.USER;
import static java.util.Arrays.asList;

/**
 * Created by PKS on 4/8/17.
 */
@Entity
public class User {

    /** -- Properties -- */

    @Id
    private String Id;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @CreationTimestamp
    private LocalDateTime timeCreated;
    @NotNull
    private Role roles;

    /** -- Constructors -- */

    private User() {
        Id = UUID.randomUUID().toString();
        timeCreated = LocalDateTime.now();
        roles = USER;
    }

    public User(String Id, String username, LocalDateTime timeCreated, Role roles) {
        this.Id = Id;
        this.username = username;
        this.timeCreated = timeCreated;
        this.roles = roles;
    }

    public User(UserRegistrationView registrationView) {
        this();
        username = registrationView.getUsername();
        email = registrationView.getEmail();
        firstName = registrationView.getFirstName();
        lastName = registrationView.getLastName();
        password = registrationView.getPassword();
    }

    /** -- Getters & Setters -- */

    public String getId() {
        return Id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public Role getRoles() {
        return roles;
    }

}
