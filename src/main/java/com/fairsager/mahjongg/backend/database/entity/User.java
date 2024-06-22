package com.fairsager.mahjongg.backend.database.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "app_user")
public class User {

    public User(UUID createdBy) {
        this.registered = new Date();
        this.createdBy = createdBy;
    }

    @Id
    @UuidGenerator
    private UUID userId;

    private String username;
    private String displayName;

    private String biography;

    private String avatar;

    @Setter(AccessLevel.NONE)
    private Date registered;
    private Date lastSeen;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String passwordHash;
    private String salt;

    @Setter(AccessLevel.NONE)
    private UUID createdBy;

    public enum Role {
        USER,
        DEV
    }
}

