package com.fairsager.mahjongg.backend.database.repository;

import com.fairsager.mahjongg.backend.database.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    User findByUsername(String username);

    User findByUserId(UUID userId);

    List<User> findByUsernameContainingOrDisplayNameContaining(String query, String query2);
}
