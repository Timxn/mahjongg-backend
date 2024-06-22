package com.fairsager.mahjongg.backend.models.v1.user;

import com.fairsager.mahjongg.backend.database.entity.User;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserModel {

    private UUID userId;

    private String username;
    private String displayName;

    private String biography;

    private Date registered;
    private Date lastSeen;

    private User.Role role;
}
