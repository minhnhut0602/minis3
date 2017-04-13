package com.pksprojects.minis3.repositories;

import com.pksprojects.minis3.models.user.User;

/**
 * Interface Used to do reflections on MetaData retrieval from database.
 * Created by PKS on 4/10/17.
 */
public interface OnlyNameAndUserId {

    String getName();

    User getUser();
}
