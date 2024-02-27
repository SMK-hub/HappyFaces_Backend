package com.example.Demo.AdminServices;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.example.Demo.Enum.EnumClass;
import com.example.Demo.Model.*;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {

    List<Admin> getAllAdmin();

    List<Orphanage> getAllOrphanage();

    List<OrphanageDetails> getAllOrphanageDetails();

    List<Donor> getAllDonor();

    List<Events> getAllEvents();

    List<Donations> getAllDonations();

    List<DonationRequirements> getAllRequirements();

    String registerUser(Admin user);

    boolean loginUser(String email, String password);

    String addProfilePhoto(String adminId, MultipartFile file) throws IOException;

    String getProfilePhoto(String adminId);

    void updateProfilePhoto(String adminId, MultipartFile file) throws IOException;

    OrphanageDetails getOrphanageDetailByOrphanageId(String orpId);

    OrphanageDetails verifyOrphanageDetails(String orpId, String verificationStatus);

    Events verifyEventDetails(String eventId, String verificationStatus);

    Admin editProfile(String adminId, Admin admin);

//    String verifyEventDetails(Events event);

    List<InterestedPerson> getInterestedPersonByEventId(String eventId);

    Orphanage getOrphanageById(String id);

    Donor getDonorById(String id);

    Admin getAdminById(String id);

    Events getEventById(String id);

    List<Events> getEventsByOrphanageId(String id);

    Donations getDonationById(String id);

    List<Donations> getDonationsByOrphanageId(String id);

    List<Donations> getDonationsByDonorId(String id);

    String sendOtp(Admin admin);

    String forgetPassword(String email, String otp, String create, String confirm);

    Admin changeAdminPassword(String email, String oldPassword, String newPassword, String confirmNewPassword);

    Optional<Admin> getAdminByEmail(String email);

    public byte[] getCertificate(String orpId);
}