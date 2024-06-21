package com.fairsager.mahjongg.backend.database.repository;

import com.fairsager.mahjongg.backend.database.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
}
