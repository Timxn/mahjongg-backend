package com.fairsager.mahjongg.backend.endpoints.v1.rest;

import com.fairsager.mahjongg.backend.annotations.DefaultAuthentication;
import com.fairsager.mahjongg.backend.annotations.DefaultAuthorization;
import com.fairsager.mahjongg.backend.annotations.Right;
import com.fairsager.mahjongg.backend.database.entity.Session;
import com.fairsager.mahjongg.backend.models.v1.user.UserModel;
import com.fairsager.mahjongg.backend.service.v1.api.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping(path = "/api/v1/user")
public class UserController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserService userService;

    @DefaultAuthentication
    @GetMapping(path = "byUsername")
    public ResponseEntity<?> getUserByUsername(@RequestParam(name = "username") String username) {
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
    }

    @DefaultAuthentication
    @GetMapping()
    public ResponseEntity<?> getUserByUserId(@RequestParam(name = "userId") UUID userId) {
        return new ResponseEntity<>(userService.getUserByUserId(userId), HttpStatus.OK);
    }

    @DefaultAuthentication
    @GetMapping(path = "search")
    public ResponseEntity<?> searchUsers(@RequestParam(name = "query") String query) {
        return new ResponseEntity<>(userService.searchUsers(query), HttpStatus.OK);
    }

    @DefaultAuthentication
    @DefaultAuthorization(right = Right.DEV)
    @GetMapping(path = "create")
    public ResponseEntity<?> createUser() {
        Session session = (Session) request.getSession().getAttribute("session");
        return new ResponseEntity<>(userService.createUser(session.getUserId()), HttpStatus.CREATED);
    }

    @GetMapping(path = "salt")
    public ResponseEntity<?> getSalt(@RequestParam(name = "username") String username) {
        return new ResponseEntity<>(userService.getSalt(username), HttpStatus.OK);
    }

    @DefaultAuthentication
    @DefaultAuthorization(right = Right.DEV)
    @GetMapping(path = "resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam(name = "userId") UUID userId) {
        userService.resetPassword(userId);
        return new ResponseEntity<>(HttpStatus.OK, HttpStatus.OK);
    }

    @GetMapping(path = "changePassword")
    public ResponseEntity<?> changePassword(@RequestParam(name = "username") String username, @RequestParam(name = "newPassword") String newPassword, @RequestParam(name = "newSalt") String newSalt) {
        userService.changePassword(username, newPassword, newSalt);
        return new ResponseEntity<>(HttpStatus.OK, HttpStatus.OK);
    }

    @DefaultAuthentication
    @DefaultAuthorization(right = Right.OWN)
    @PutMapping(path = "edit")
    public ResponseEntity<?> editUser(@RequestBody UserModel userModel) {
        return new ResponseEntity<>(userService.editUser(userModel), HttpStatus.OK);
    }

    @GetMapping(path = "usernameExists")
    public ResponseEntity<?> usernameExists(@RequestParam(name = "username") String username) {
        return new ResponseEntity<>(userService.usernameExists(username), HttpStatus.OK);
    }
}
