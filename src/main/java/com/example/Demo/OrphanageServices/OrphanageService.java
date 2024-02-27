package com.example.Demo.OrphanageServices;

import com.example.Demo.Model.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface OrphanageService {

    String registerUser(Orphanage user);

    boolean loginUser(String email, String password);

    Optional<OrphanageDetails> getDetailById(String orpId);

    String updateDetails(OrphanageDetails details);

    void addProfilePhoto(String orphanageId, MultipartFile file) throws IOException;

    String getProfilePhoto(String orphanageId);

    void updateProfilePhoto(String orphanageId, MultipartFile file) throws IOException;

    String createEvents(Events event);

    String editEvent(String eventId, Events event);

    List<Events> getPlannedEvents(String orpId);

    String cancelEvent(String eventId);

    List<InterestedPerson> getInterestedPersonByEventId(String eventId);

    String sendOtp(Orphanage orphanage);

    public String forgetPassword(String email, String otp, String create, String confirm);

    Orphanage editProfile(String orphanageId, Orphanage orphanage);

    String editDetails(String orphanageId, OrphanageDetails orphanageDetails);

    void uploadImages(String orphanageId, List<MultipartFile> imageFiles) throws IOException;

    List<OrphanageImage> getOrphanageImagesById(String orphanageId);

    Optional<Orphanage> changeOrphanagePassword(String email, String oldPassword, String newPassword, String confirmNewPassword);

    void removeImage(String orphanageId, String imageId);

    Optional<Orphanage> getOrphanageByEmail(String email);

    public String storeCertificate(String orpId, MultipartFile file) throws IOException;

    public byte[] getCertificate(String orpId);

    public List<Donations> getDonationsByOrphanageId(String id);

    public Donor getDonorById(String donorId);

    Integer getViewCount(String orpId);
}
