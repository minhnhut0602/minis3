package com.pksprojects.minis3.repositories;

import com.pksprojects.minis3.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by PKS on 4/8/17.
 */
@Repository
public interface UsersRepository extends JpaRepository<User, String>{

    public User findUserByUsername(String username);

    public User findUserByEmail(String email);
}
