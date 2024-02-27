package com.example.Demo.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.Demo.Model.Orphanage;

public interface OrphanageRepository extends MongoRepository<Orphanage, String> {
    Optional<Orphanage> findByEmail(String email);
}