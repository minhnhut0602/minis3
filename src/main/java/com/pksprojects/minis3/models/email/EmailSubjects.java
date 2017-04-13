package com.pksprojects.minis3.models.email;

/**
 * Enum provides Email subjects template.
 * Created by PKS on 4/10/17.
 */
public enum EmailSubjects {

    HOURLY_UPDATES("Your hourly update!"),
    REGISTRATION_SUCCESS("Registration Successful"),
    ;

    String subject;

    EmailSubjects(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }
}
