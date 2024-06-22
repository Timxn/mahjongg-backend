package com.fairsager.mahjongg.backend.models.v1.user;

import com.fairsager.mahjongg.backend.database.entity.User;
import lombok.Data;

import java.util.UUID;

@Data
public class UserShortModel {

    private UUID userId;

    private String username;
    private String displayName;

    private User.Role role;
}
