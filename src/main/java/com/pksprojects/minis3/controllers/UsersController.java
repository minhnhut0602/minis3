package com.pksprojects.minis3.controllers;

import com.pksprojects.minis3.models.view.user.UserRegistrationView;
import com.pksprojects.minis3.services.UsersService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by PKS on 4/8/17.
 */
@RestController
@RequestMapping("api/v1/users")
public class UsersController {

    private static final Logger logger = LogManager.getLogger(UsersController.class);

    @Autowired
    private UsersService usersService;

    @RequestMapping(value = "/register", method = POST)
    @ResponseBody
    public void register(@RequestBody @Valid UserRegistrationView user){
        usersService.registerUser(user);
        logger.info("User: " + user.getUsername() + " successfully registered");
    }

    @RequestMapping(value = "/isavailable/{username}", method = GET)
    @ResponseBody
    public ResponseEntity<Boolean> isAvailable(@PathVariable String username) {
        return ResponseEntity.ok(usersService.isAvailable(username));
    }

}
