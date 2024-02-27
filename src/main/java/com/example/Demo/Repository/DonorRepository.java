package com.example.Demo.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.Demo.Model.Donor;

public interface DonorRepository extends MongoRepository<Donor, String>{

    Optional<Donor> findByEmail(String email);


	
}
