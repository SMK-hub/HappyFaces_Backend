package com.example.Demo.DonorServices;

import com.example.Demo.Model.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DonorService {

    String registerUser(Donor user);

    boolean loginUser(String email, String password);

    Optional<Donor> viewProfile(String donorId);

    Donor editProfile(String donorId, Donor donor);

    Donor addProfilePhoto(String donorId, MultipartFile file) throws IOException;

    String getProfilePhoto(String donorId);

    String sendOtp(Donor donor);

    String forgetPassword(String email, String otp, String create, String confirm);

    List<OrphanageDetails> getVerifiedOrphanageDetails();

    Optional<OrphanageDetails> getVerifiedOrphanageDetailsById(String orpId);

    List<Events> getVerifiedEvents(String orpId);

    String eventRegister(String eventId, String donorId);

    String cancelEventRegistration(String eventId, String donorId);

    Optional<Donor> getDonorByEmail(String email);

    Donor changeDonorPassword(String email, String oldPassword, String newPassword, String confirmNewPassword);
    Donations saveDonationDetail(Donations donations);

    List<String> getDonorIdFromEvent(String eventId);

    String saveDonationRequirements(DonationRequirements donationRequirements);
    List<DonationRequirements> getDonationRequirementsByDonorId(String donorId);

    List<DonationRequirements> getAllDonationRequirementByDonorId(String donorId);
    List<InterestedPerson> findAllInterestedPersonByDonorId(String donorId);
    Events findEventByEventId(String eventId);
    void increaseViewCount(String orpId);
}
