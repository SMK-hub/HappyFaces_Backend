package com.example.Demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.Demo.Model.Requirements;

public interface RequirementsRepository extends MongoRepository<Requirements,String>{

}
