package com.example.Demo.Repository;

import com.example.Demo.Model.DonationRequirements;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DonationRequirementRepository extends MongoRepository<DonationRequirements,String> {

    List<DonationRequirements> findAllDonationsRequirementByDonorId(String donorId);

    List<DonationRequirements> findAllDonationRequirementByOrpId(String orpId);

}
