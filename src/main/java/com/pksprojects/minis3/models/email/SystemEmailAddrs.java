package com.pksprojects.minis3.models.email;

/**
 * Created by PKS on 4/10/17.
 */
public enum SystemEmailAddrs {

    HOURLY_NOTIFICATION("notification@yourdomain.com"),
    REGISTRATION_SUCCESS("welcome@yourdomain.com")
    ;

    String email;

    SystemEmailAddrs(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
