package com.example.Demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.Demo.Model.Events;

import java.util.List;
import java.util.Optional;

public interface EventsRepository extends MongoRepository<Events, String>{
    List<Events> getEventsByOrpId(String orpId);
    List<Events> findByVerificationStatusAndOrpId(String verificationStatus,String orpId);

    Optional<Events> findByOrpId(String orpId);
}
