package com.example.Demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.Demo.Model.OrphanageDetails;

public interface OrphanageDetailsRepository extends MongoRepository<OrphanageDetails, String>{

    List<OrphanageDetails> findAllById(String orpId);
    Optional<OrphanageDetails> findByOrpId(String orpId);
    List<OrphanageDetails> findByVerificationStatus(String verificationStatus);
}
