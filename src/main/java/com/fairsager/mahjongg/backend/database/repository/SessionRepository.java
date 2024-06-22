package com.fairsager.mahjongg.backend.database.repository;


import com.fairsager.mahjongg.backend.database.entity.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SessionRepository extends CrudRepository<Session, UUID> {
}
