package com.pksprojects.minis3.services;

import com.pksprojects.minis3.models.user.User;
import com.pksprojects.minis3.models.view.user.UserRegistrationView;
import com.pksprojects.minis3.repositories.FileRepository;
import com.pksprojects.minis3.repositories.UsersRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Provides User tasks related service
 * Created by PKS on 4/8/17.
 */
@Service
public class UsersService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(UsersService.class);

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Method used for authenticating user
     * @param username of user trying to log in
     * @return UserDetails if user with username exists
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userFromDatabase =  Optional.ofNullable(usersRepository.findUserByUsername(username));
        return userFromDatabase.map(user -> {
            GrantedAuthority authority = new SimpleGrantedAuthority(user.getRoles().name());
            return new org.springframework.security.core.userdetails.User(username,
                    user.getPassword(),
                    Collections.singletonList(authority));
        }).orElseThrow(() -> new UsernameNotFoundException("User " + username + " was not found in the database"));
    }

    /**
     * Performs user registration
     * @param registrationView of user to be registered
     * @return true if success else false
     */
    public boolean registerUser(UserRegistrationView registrationView){
        if(!isAvailable(registrationView.getUsername()) || !isEmailAlreadyInUser(registrationView.getEmail()))
            return false;
        try {
            User user = new User(registrationView);
            user.setPassword(passwordEncoder.encode(registrationView.getPassword()));
            usersRepository.save(user);
            fileRepository.createDirectory(user.getId());
            return true;
        }catch (Exception ex){
            logger.debug("Unexpected Exception: ", ex);
            return false;
        }
    }

    /**
     * checks if the username is available
     * @param username to be checked
     * @return true is available else false
     */
    public boolean isAvailable(String username){
        return usersRepository.findUserByUsername(username) == null;
    }

    /**
     * Checks if the email address is already used with another account
     * @param email to be checked
     * @return true is email not used else false
     */
    private boolean isEmailAlreadyInUser(String email) {
        return usersRepository.findUserByEmail(email) == null;
    }

    /**
     * Gets current logged In user.
     * @return User
     */
    public User getCurrentUser() {
        return getUser();
    }

    /**
     * Gets username of current authenticated user
     * @return String username
     */
    private String getUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * Get currently authenticated user from database.
     * @return User current user
     */
    private User getUser() {
        return usersRepository.findUserByUsername(getUsername());
    }
}
