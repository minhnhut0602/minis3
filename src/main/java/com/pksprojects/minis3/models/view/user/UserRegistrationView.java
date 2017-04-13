package com.pksprojects.minis3.models.view.user;

/**
 * Registration view of user.
 * Created by PKS on 4/9/17.
 */
public class UserRegistrationView {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    public UserRegistrationView() {}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
