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

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserModel getUserByUsername(String username) {
        if (username.trim().isBlank())
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Username cannot be empty", getClass());
        User user = userRepository.findByUsername(username.trim());
        if (user == null)
            throw new ServiceException(HttpStatus.NOT_FOUND, "User not found", getClass());
        return c2UserModel(user);
    }

    @Override
    public UserModel getUserByUserId(UUID userId) {
        if (userId == null)
            throw new ServiceException(HttpStatus.BAD_REQUEST, "User ID cannot be empty", getClass());
        User user = userRepository.findByUserId(userId);
        if (user == null)
            throw new ServiceException(HttpStatus.NOT_FOUND, "User not found", getClass());
        return c2UserModel(user);
    }

    @Override
    public List<UserShortModel> searchUsers(String query) {
        List<User> users = userRepository.findByUsernameContainingOrDisplayNameContaining(query.trim(), query.trim());
        List<UserShortModel> response = new ArrayList<>();
        for (User user : users) {
            response.add(c2UserShortModel(user));
        }
        return response;
    }

    @Override
    public void createUser(UUID creatorId) {
        if (creatorId == null)
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Creator ID cannot be empty", getClass());
        userRepository.save(new User(creatorId));
    }

    @Override
    public String getSalt(String username) {
        if (username.trim().isBlank())
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Username cannot be empty", getClass());
        User user = userRepository.findByUsername(username.trim());
        return user.getSalt();
    }

    @Override
    public void resetPassword(UUID userId) {
        if (userId == null)
            throw new ServiceException(HttpStatus.BAD_REQUEST, "User ID cannot be empty", getClass());
        User user = userRepository.findByUserId(userId);
        user.setPasswordHash(null);
        user.setSalt(null);
        userRepository.save(user);
    }

    @Override
    public void changePassword(UUID userId, String newPassword, String newSalt) {
        if (userId == null)
            throw new ServiceException(HttpStatus.BAD_REQUEST, "User ID cannot be empty", getClass());
        if (newPassword.trim().isBlank())
            throw new ServiceException(HttpStatus.BAD_REQUEST, "New password cannot be empty", getClass());
        if (newSalt.trim().isBlank())
            throw new ServiceException(HttpStatus.BAD_REQUEST, "New salt cannot be empty", getClass());
        User user = userRepository.findByUserId(userId);
        user.setPasswordHash(newPassword);
        user.setSalt(newSalt);
        user.setLastSeen(new Date());
        userRepository.save(user);
    }

    @Override
    public void editUser(UserModel userModel) {
        if (userModel.getUserId() == null)
            throw new ServiceException(HttpStatus.BAD_REQUEST, "User ID cannot be empty", getClass());
        if (userModel.getUsername().trim().isBlank())
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Username cannot be empty", getClass());
        User user = userRepository.findByUserId(userModel.getUserId());
        user.setUsername(userModel.getUsername().trim());
        user.setDisplayName(userModel.getDisplayName().trim());
        user.setBiography(userModel.getBiography().trim());
        user.setLastSeen(new Date());
        userRepository.save(user);
    }

    private UserModel c2UserModel(User user) {
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

    private UserShortModel c2UserShortModel(User user) {
        UserShortModel response = new UserShortModel();
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setDisplayName(user.getDisplayName());
        response.setRole(user.getRole());
        return response;
    }
}
