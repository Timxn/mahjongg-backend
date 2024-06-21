package com.fairsager.mahjongg.backend.database.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @UuidGenerator
    private UUID userId;

    private String username;
    private String displayName;

    private String biography;

    private String avatar;

    private Date registered;
    private Date lastSeen;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String passwordHash;
    private String salt;

    public enum Role {
        USER,
        DEV
    }
}

