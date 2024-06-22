package com.fairsager.mahjongg.backend.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
public class Session {

    @Id
    @UuidGenerator
    private UUID sessionId;

    private UUID userId;
    private String username;
    private String displayName;
    private User.Role role;

    private String clientInfo;
    private Date firstSeen;
    private Date lastSeen;
    private boolean active;

    public Session() {
        active = true;
    }
}
