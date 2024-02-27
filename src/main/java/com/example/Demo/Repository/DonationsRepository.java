package com.example.Demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.Demo.Model.Donations;

import java.util.List;

public interface DonationsRepository extends MongoRepository<Donations, String>{

    List<Donations> getDonationsByOrpId(String orpId);
    List<Donations> getDonationsByDonorId(String donorId);
}
