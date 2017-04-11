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

    public boolean isAvailable(String username){
        return usersRepository.findUserByUsername(username) == null;
    }

    private boolean isEmailAlreadyInUser(String email) {
        return usersRepository.findUserByEmail(email) == null;
    }

    public String getCurrentUsername() {
        return getUsername();
    }

    public String getCurrentUserId() {
        return getUserId();
    }

    public User getCurrentUser() {
        return getUser();
    }

    private String getUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return null;
    }

    private String getUserId(){
        return getUser().getId();
    }

    private User getUser() {
        return usersRepository.findUserByUsername(getUsername());
    }
}
