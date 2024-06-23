package com.fairsager.mahjongg.backend.service.v1.impl;

import com.fairsager.mahjongg.backend.database.entity.User;
import com.fairsager.mahjongg.backend.database.repository.UserRepository;
import com.fairsager.mahjongg.backend.exception.ServiceException;
import com.fairsager.mahjongg.backend.models.v1.user.UserModel;
import com.fairsager.mahjongg.backend.models.v1.user.UserShortModel;
import com.fairsager.mahjongg.backend.service.v1.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.fairsager.mahjongg.backend.utils.Validator.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserModel getUserByUsername(String username) {
        validateString(username, "Username cannot be empty", getClass());
        User user = userRepository.findByUsername(username.trim());
        if (user == null)
            throw new ServiceException(HttpStatus.NOT_FOUND, "User not found", getClass());
        return convertToUserModel(user);
    }

    @Override
    public UserModel getUserByUserId(UUID userId) {
        validateUUID(userId, "User ID cannot be empty", getClass());
        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User not found", getClass()));
        return convertToUserModel(user);
    }

    @Override
    public List<UserShortModel> searchUsers(String query) {
        List<User> users = userRepository.findByUsernameContainingOrDisplayNameContaining(query.trim(), query.trim());
        List<UserShortModel> response = new ArrayList<>();
        for (User user : users) {
            response.add(convertToUserShortModel(user));
        }
        return response;
    }

    @Override
    public UUID createUser(UUID creatorId) {
        validateUUID(creatorId, "Creator ID cannot be empty", getClass());
        User user = new User(creatorId);
        userRepository.save(user);
        return user.getUserId();
    }

    @Override
    public String getSalt(String username) {
        validateString(username, "Username cannot be empty", getClass());
        User user = userRepository.findByUsername(username.trim());
        if (user == null)
            throw new ServiceException(HttpStatus.NOT_FOUND, "User not found", getClass());
        return user.getSalt();
    }

    @Override
    public void resetPassword(UUID userId) {
        validateUUID(userId, "User ID cannot be empty", getClass());
        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User not found", getClass()));
        user.setPasswordHash(null);
        user.setSalt(null);
        userRepository.save(user);
    }

    @Override
    public void changePassword(String username, String newPassword, String newSalt) {
        validateString(username, "Username cannot be empty", getClass());
        validateString(newPassword, "New password cannot be empty", getClass());
        validateString(newSalt, "New salt cannot be empty", getClass());
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new ServiceException(HttpStatus.NOT_FOUND, "User not found", getClass());
        if (user.getPasswordHash() != null && !user.getPasswordHash().isBlank() && user.getSalt() != null && !user.getSalt().isBlank())
            throw new ServiceException(HttpStatus.FORBIDDEN, "The password has already been set", getClass());
        user.setPasswordHash(newPassword);
        user.setSalt(newSalt);
        user.setLastSeen(new Date());
        userRepository.save(user);
    }

    @Override
    public UserModel editUser(UserModel userModel) {
        validateUUID(userModel.getUserId(), "User ID cannot be empty", getClass());
        validateUsername(userModel.getUsername(), getClass());
        User user = userRepository.findById(userModel.getUserId()).orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User not found", getClass()));
        user.setUsername(userModel.getUsername().trim().toLowerCase());
        user.setDisplayName(userModel.getDisplayName().trim());
        user.setBiography(userModel.getBiography().trim());
        user.setLastSeen(new Date());
        userRepository.save(user);
        return convertToUserModel(user);
    }

    @Override
    public Boolean usernameExists(String username) {
        validateString(username, "Username cannot be empty", getClass());
        return userRepository.existsByUsername(username.trim().toLowerCase());
    }

    private UserModel convertToUserModel(User user) {
        UserModel response = new UserModel();
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setDisplayName(user.getDisplayName());
        response.setBiography(user.getBiography());
        response.setRegistered(user.getRegistered());
        response.setLastSeen(user.getLastSeen());
        response.setRole(user.getRole());
        return response;
    }

    private UserShortModel convertToUserShortModel(User user) {
        UserShortModel response = new UserShortModel();
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setDisplayName(user.getDisplayName());
        response.setRole(user.getRole());
        return response;
    }
}
