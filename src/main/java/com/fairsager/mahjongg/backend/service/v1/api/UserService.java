package com.fairsager.mahjongg.backend.service.v1.api;

import com.fairsager.mahjongg.backend.models.v1.user.UserModel;
import com.fairsager.mahjongg.backend.models.v1.user.UserShortModel;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserModel getUserByUsername(String username);

    UserModel getUserByUserId(UUID userId);

    List<UserShortModel> searchUsers(String query);

    UUID createUser(UUID creatorId);

    String getSalt(String username);

    void resetPassword(UUID userId);

    void changePassword(String username, String newPassword, String newSalt);

    UserModel editUser(UserModel userModel);
}
