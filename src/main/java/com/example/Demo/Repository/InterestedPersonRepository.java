package com.example.Demo.Repository;

import com.example.Demo.Model.InterestedPerson;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InterestedPersonRepository extends MongoRepository<InterestedPerson,String> {

    List<InterestedPerson> findAllInterestedPersonByEventId(String eventId);

    List<InterestedPerson> findAllInterestedPersonByDonorId(String donorId);

}
