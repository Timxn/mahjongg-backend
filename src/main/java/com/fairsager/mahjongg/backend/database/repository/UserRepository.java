package com.fairsager.mahjongg.backend.database.repository;

import com.fairsager.mahjongg.backend.database.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    User findByUsername(String username);

    List<User> findByUsernameContainingOrDisplayNameContaining(String query, String query2);

    Boolean existsByUsername(String username);

    Boolean existsByUserIdAndUsernameIsEmpty(UUID userId);
}
