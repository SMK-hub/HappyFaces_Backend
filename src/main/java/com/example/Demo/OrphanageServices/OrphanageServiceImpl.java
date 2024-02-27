package com.example.Demo.OrphanageServices;

import com.example.Demo.EmailServices.EmailService;
import com.example.Demo.Enum.EnumClass;
import com.example.Demo.Enum.EnumClass.VerificationStatus;
import com.example.Demo.Model.*;
import com.example.Demo.Repository.*;
import jdk.jfr.Event;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrphanageServiceImpl implements OrphanageService {

    @Autowired
    private OrphanageRepository orphanageRepository;
    @Autowired
    private DonationsRepository donationsRepo;
    @Autowired
    private DonorRepository donorRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private EventsRepository eventsRepository;
    @Autowired
    private OrphanageDetailsRepository detailRepository;
    @Autowired
    private InterestedPersonRepository interestedPersonRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private OrphanageImageRepository orphanageImageRepository;
    @Autowired
    private OrphanageDetailsRepository orphanageDetailsRepository;

    public void saveUser(Optional<Orphanage> optionalOrphanage) {
        optionalOrphanage.ifPresent(orphanage -> {
            orphanageRepository.save(orphanage);
        });
    }

    public String registerUser(Orphanage newUser) {
        Optional<Orphanage> user = orphanageRepository.findByEmail(newUser.getEmail());
        Optional<Donor> donorUser=donorRepository.findByEmail(newUser.getEmail());
        Optional<Admin> adminUser=adminRepository.findByEmail(newUser.getEmail());

        if (user.isEmpty() && donorUser.isEmpty() && adminUser.isEmpty()) {
            newUser.setRole(EnumClass.Roles.ORPHANAGE);
            orphanageRepository.save(newUser);
            String subject = "Registration Successful";
            String body = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "  <meta charset=\"UTF-8\">\n" +
                    "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "  <title>Happy Faces - Welcome, [Orphanage Name]!</title>\n" +
                    "  <style>\n" +
                    "    body {\n" +
                    "      font-family: Arial, sans-serif;\n" +
                    "      margin: 20px;\n" +
                    "      background-color: #f4f4f4;\n" +
                    "    }\n" +
                    "    .container {\n" +
                    "      max-width: 600px;\n" +
                    "      margin: 0 auto;\n" +
                    "      background-color: #fff;\n" +
                    "      padding: 20px;\n" +
                    "      border-radius: 5px;\n" +
                    "      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\n" +
                    "    }\n" +
                    "    h2 {\n" +
                    "      margin-top: 0;\n" +
                    "      color: #222;\n" +
                    "    }\n" +
                    "    p {\n" +
                    "      font-size: 16px;\n" +
                    "      line-height: 1.5;\n" +
                    "    }\n" +
                    "    .logo {\n" +
                    "      max-width: 150px;\n" +
                    "      display: block;\n" +
                    "      margin-bottom: 20px;\n" +
                    "    }\n" +
                    "    .button {\n" +
                    "      display: inline-block;\n" +
                    "      padding: 8px 16px;\n" +
                    "      font-size: 16px;\n" +
                    "      background-color: #007bff;\n" +
                    "      color: #fff;\n" +
                    "      border: none;\n" +
                    "      border-radius: 5px;\n" +
                    "      cursor: pointer;\n" +
                    "      text-decoration: none;\n" +
                    "    }\n" +
                    "  </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "  <div class=\"container\">\n" +
                    "     <h2>Welcome to the Happy Faces Family, "+newUser.getName()+"!</h2>\n" +
                    "    <p>We are thrilled to welcome you to our community of dedicated individuals and organizations committed to supporting children in need.</p>\n" +
                    "    <p>Your decision to register your orphanage with Happy Faces demonstrates your unwavering commitment to providing a safe and loving environment for the children under your care.</p>\n" +
                    "    <p>With your participation, we can collectively create a brighter future for these children by offering them access to resources, opportunities, and support that will help them thrive.</p>\n" +
                    "    <p>Whether it's volunteering, fundraising, or simply connecting with other like-minded organizations, there are many ways you can contribute to our mission.</p>\n" +
                    "    <p>We encourage you to explore our website and resources to learn more about how you can get involved and make a difference.</p>\n" +
                    "    <p>Thank you for choosing to partner with Happy Faces. Together, we can build a world where every child has the chance to reach their full potential.</p>\n" +
                    "    <p>Sincerely,</p>\n" +
                    "    <p>The Happy Faces Team</p>\n" +
                    "  </div>\n" +
                    "</body>\n" +
                    "</html>\n";
            emailService.sendHtmlMail(newUser.getEmail(), subject, body);
            return "Success";
        } else {
            return "You are an existing user.\nPlease Login";
        }
    }

    @Override
    public boolean loginUser(String email, String password) {
        Optional<Orphanage> user = orphanageRepository.findByEmail(email);
        return user.map(orphanage -> orphanage.getPassword().equals(password)).orElse(false);
    }

    @Override
    public String updateDetails(OrphanageDetails details) {
        Optional<OrphanageDetails> orphanageDetails = orphanageDetailsRepository.findByOrpId(details.getOrpId());
        if(orphanageDetails.isPresent()) {
            orphanageDetails.get().setVerificationStatus(VerificationStatus.NOT_VERIFIED);
            detailRepository.save(orphanageDetails.get());
            return "Updated Successfully";
        }
        else{
            details.setVerificationStatus(VerificationStatus.NOT_VERIFIED);
            detailRepository.save(details);
            return "Details Added successfully";
        }
    }
    @Override
    public void addProfilePhoto(String orphanageId, MultipartFile file) throws IOException {
        byte[] photoBytes = file.getBytes();
        System.out.println();

        Orphanage orphanage= orphanageRepository.findById(orphanageId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        orphanage.setProfilePhoto(photoBytes);
        orphanageRepository.save(orphanage);
    }

    @Override
    public String getProfilePhoto(String orphanageId) {
        Orphanage orphanage= orphanageRepository.findById(orphanageId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        byte[] photoBytes = orphanage.getProfilePhoto();
        if (photoBytes != null && photoBytes.length > 0) {
            return Base64.getEncoder().encodeToString(photoBytes);
        } else {
            return null; // Or return a default image URL or handle it based on your requirements
        }
    }

    public void updateProfilePhoto(String orphanageId, MultipartFile file) throws IOException {
        byte[] newPhotoBytes = file.getBytes();
        Orphanage orphanage= orphanageRepository.findById(orphanageId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        orphanage.setProfilePhoto(newPhotoBytes);
        orphanageRepository.save(orphanage);
    }

    @Override
    public String createEvents(Events event) {
        Optional<Events> oldEvent = eventsRepository.getEventsByOrpId(event.getOrpId())
                .stream()
                .filter(xEvent-> xEvent.getEventStatus().equals(EnumClass.EventStatus.PLANNED) || xEvent.getEventStatus().equals(EnumClass.EventStatus.ONGOING))
                .filter(xEvent-> xEvent.getTitle().equals(event.getTitle()))
                .findAny();
        if(oldEvent.isEmpty()) {
            event.setEventStatus(EnumClass.EventStatus.PLANNED);
            event.setVerificationStatus(EnumClass.VerificationStatus.NOT_VERIFIED);
            eventsRepository.save(event);
            return "Event Created";
        }
        return null;
    }
    @Override
    public String editEvent(String eventId,Events event){
        Optional<Events> optionalEvents=eventsRepository.findById(eventId);
        if(optionalEvents.isPresent()){
            optionalEvents.get().setVerificationStatus(VerificationStatus.NOT_VERIFIED);
            optionalEvents.get().setTitle(event.getTitle());
            optionalEvents.get().setTime(event.getTime());
            optionalEvents.get().setDate(event.getDate());
            optionalEvents.get().setDescription(event.getDescription());
            eventsRepository.save(optionalEvents.get());
            return "Event Updated Successfully";
        }
        return "Error occurred try again";
    }

    @Override
    public List<Events> getPlannedEvents(String orpId) {
        List<Events> events= eventsRepository.getEventsByOrpId(orpId);
        if(events == null || events.isEmpty()){
            return null;
        }
        return (List<Events>) events.stream()
                .filter(event->event.getEventStatus().equals(EnumClass.EventStatus.PLANNED )
                           ||  event.getEventStatus().equals(EnumClass.EventStatus.ONGOING))
                .collect(Collectors.toList());
    }

    @Override
    public String cancelEvent(String eventId) {
        Optional<Events> optionalEvent = eventsRepository.findById(eventId);
        if(optionalEvent.isPresent()){
            optionalEvent.get().setEventStatus(EnumClass.EventStatus.CANCELLED);
            eventsRepository.save(optionalEvent.get());
            List<InterestedPerson> interestedPersonList = interestedPersonRepository.findAllInterestedPersonByEventId(eventId);
            for(InterestedPerson person:interestedPersonList){
                String email = person.getEmail();
                String subject = "Event Cancelled";
                String body = "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "<meta charset=\"UTF-8\">\n" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "<title>Event Cancellation</title>\n" +
                        "<style>\n" +
                        "  body {\n" +
                        "    font-family: Arial, sans-serif;\n" +
                        "    margin: 0;\n" +
                        "    padding: 0;\n" +
                        "    background-color: #f8f8f8;\n" +
                        "  }\n" +
                        "  .container {\n" +
                        "    max-width: 600px;\n" +
                        "    margin: 20px auto;\n" +
                        "    padding: 20px;\n" +
                        "    background-color: #ffffff;\n" +
                        "    border-radius: 5px;\n" +
                        "    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                        "  }\n" +
                        "  h1, p {\n" +
                        "    margin: 0 0 20px;\n" +
                        "  }\n" +
                        "  .button {\n" +
                        "    display: inline-block;\n" +
                        "    padding: 10px 20px;\n" +
                        "    background-color: #007bff;\n" +
                        "    color: #ffffff;\n" +
                        "    text-decoration: none;\n" +
                        "    border-radius: 3px;\n" +
                        "  }\n" +
                        "</style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<div class=\"container\">\n" +
                        "  <h1>Event Cancellation</h1>\n" +
                        "  <p>Dear "+person.getName()+",</p>\n" +
                        "  <p>We regret to inform you that the event <strong>"+optionalEvent.get().getTitle()+"</strong> has been cancelled due to unforeseen circumstances. We apologize for any inconvenience this may cause.</p>\n" +
                        "  <p>We appreciate your interest and participation, and we sincerely apologize for any disappointment this cancellation may bring. Your support means a lot to us, and we hope to have the opportunity to welcome you to future events.</p>\n" +
                        "  <p>Once again, we apologize for any inconvenience caused and thank you for your understanding.</p>\n" +
                        "  <p>Best regards,<br>[Donor Name]</p>\n" +
                        "</div>\n" +
                        "</body>\n" +
                        "</html>";
                emailService.sendHtmlMail(email,subject,body);
            }
            return "Event Cancelled Successfully";
        }
        return "Problem in Event Cancellation";
    }

    @Override
    public List<InterestedPerson> getInterestedPersonByEventId(String eventId) {
        try {
            List<InterestedPerson> interestedPersonList = interestedPersonRepository.findAllInterestedPersonByEventId(eventId);
            if(!interestedPersonList.isEmpty()){
                return interestedPersonList;
            }
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            e.printStackTrace(); // Example: Print stack trace
            // You can also rethrow a custom exception if needed
            throw new RuntimeException("Failed to retrieve interested persons by event ID: " + eventId, e);
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<OrphanageDetails> getDetailById(String orpId) {
        Optional<OrphanageDetails> optionalOrphanageDetails = detailRepository.findByOrpId(orpId);
        if (optionalOrphanageDetails.isPresent()) {
            return optionalOrphanageDetails;
        }
        return Optional.empty();
    }

    private String Otp;

    public String sendOtp(Orphanage orphanage) {

        Optional<Orphanage> user = orphanageRepository.findByEmail(orphanage.getEmail());

        if (user.isPresent()) {
            String subject = "OTP";

            String sixDigitCode = String.valueOf(10000 + new Random().nextInt(900000));

            String body = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "  <meta charset=\"UTF-8\">\n" +
                    "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "  <title>OTP Verification</title>\n" +
                    "  <style>\n" +
                    "    body {\n" +
                    "      font-family: Arial, sans-serif;\n" +
                    "      margin: 0;\n" +
                    "      padding: 20px;\n" +
                    "      background-color: #f4f4f4;\n" +
                    "    }\n" +
                    "    .container {\n" +
                    "      max-width: 600px;\n" +
                    "      margin: 0 auto;\n" +
                    "      background-color: #fff;\n" +
                    "      padding: 20px;\n" +
                    "      border-radius: 5px;\n" +
                    "      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\n" +
                    "    }\n" +
                    "    h2 {\n" +
                    "      margin-top: 0;\n" +
                    "    }\n" +
                    "    p {\n" +
                    "      font-size: 16px;\n" +
                    "    }\n" +
                    "    .otp {\n" +
                    "      display: inline-block;\n" +
                    "      padding: 8px 16px;\n" +
                    "      font-size: 18px;\n" +
                    "      background-color: #007bff;\n" +
                    "      color: #fff;\n" +
                    "      border: none;\n" +
                    "      border-radius: 5px;\n" +
                    "      cursor: pointer;\n" +
                    "      text-decoration: none;\n" +
                    "    }\n" +
                    "  </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "  <div class=\"container\">\n" +
                    "    <h2>OTP Verification</h2>\n" +
                    "    <p>Your OTP is: <strong>"+sixDigitCode+"</strong></p>\n" +
                    "    <p>Please use this OTP to verify your account.</p>\n" +
                    "    <p>If you didn't request this OTP, you can ignore this email.</p>\n" +
                    "    <p>Thank you!</p>\n" +
                    "  </div>\n" +
                    "</body>\n" +
                    "</html>\n";

            Otp = sixDigitCode;
            emailService.sendHtmlMail(user.get().getEmail(), subject, body);
            return Otp;
        }
        return null;
    }

    public String forgetPassword(String email, String otp, String create, String confirm) {
        Optional<Orphanage> user = orphanageRepository.findByEmail(email);
//	if(saved.isPresent()) {
        System.out.println("forget");
        if (user.isPresent()) {
            if (!Otp.equals(otp))
                return "OTP not matched";
            if (create.equals(confirm)) {
                user.get().setPassword(confirm);
                saveUser(user);
                return "Password Changed Successfully";
            } else {
                return "Password Do not Matched";
            }
        }
        return "user not existed";
    }

    public Orphanage editProfile(String orphanageId, Orphanage orphanage) {
        Optional<Orphanage> optionalOrphanage = orphanageRepository.findById(orphanageId);
        if (optionalOrphanage.isPresent()) {
            Orphanage existingOrphanage = optionalOrphanage.get();

            Class<?> orphanageClass = Orphanage.class;
            Field[] fields = orphanageClass.getDeclaredFields();

            for(Field field:fields){
                field.setAccessible(true);

                try{
                    Object newValue = field.get(orphanage);
                    if(newValue != null && !newValue.toString().isEmpty()){
                        field.set(existingOrphanage,newValue);
                    }
                }catch (IllegalAccessException e) {
                    e.printStackTrace(); // Handle the exception according to your needs
                }
            }

            orphanageRepository.save(existingOrphanage);
            return existingOrphanage;
        }
        return null;
    }

    @Override
    public String editDetails(String orphanageId,OrphanageDetails incomingDetails) {
        Optional<OrphanageDetails> existingDetails = detailRepository.findByOrpId(orphanageId);
        if (existingDetails.isPresent()) {
            OrphanageDetails updatedDetails = existingDetails.get();
            System.out.println(incomingDetails);
            String[] excludedFields = {"id","orpId","verificationStatus","certificate"};

            BeanUtils.copyProperties(incomingDetails, updatedDetails, excludedFields);

            // Handle nested data selectively (optional)
            if (incomingDetails.getAddress() != null) {
                BeanUtils.copyProperties(incomingDetails.getAddress(), updatedDetails.getAddress());
            }
            if (incomingDetails.getRequirements() != null) {
                BeanUtils.copyProperties(incomingDetails.getRequirements(), updatedDetails.getRequirements());
            }
            updatedDetails.setVerificationStatus(VerificationStatus.NOT_VERIFIED);
            detailRepository.save(updatedDetails);
            return "Details Updated Successfully";
        }
        else {
            OrphanageDetails newOrphanageDetails = new OrphanageDetails();
            newOrphanageDetails.setOrpId(orphanageId);
            newOrphanageDetails.setVerificationStatus(VerificationStatus.NOT_VERIFIED);
            String[] excludedFields = {"id","orpId","verificationStatus"};

            BeanUtils.copyProperties(incomingDetails, newOrphanageDetails, excludedFields);

            if (incomingDetails.getAddress() != null) {
                BeanUtils.copyProperties(incomingDetails.getAddress(), newOrphanageDetails.getAddress());
            }
            if (incomingDetails.getRequirements() != null) {
                BeanUtils.copyProperties(incomingDetails.getRequirements(), newOrphanageDetails.getRequirements());
            }

            detailRepository.save(newOrphanageDetails);
            return "Details Updated Successfully";
        }

    }
    @Override
    public void uploadImages(String orphanageId, List<MultipartFile> imageFiles) throws IOException {
        for (MultipartFile file : imageFiles) {
            byte[] imageData = file.getBytes();
            OrphanageImage orphanageImage = new OrphanageImage();
            orphanageImage.setOrphanageId(orphanageId);
            orphanageImage.setImage(imageData);
            orphanageImageRepository.save(orphanageImage);
        }
    }

    @Override
    public List<OrphanageImage> getOrphanageImagesById(String orphanageId) {
        return orphanageImageRepository.findOrphanageImageByOrphanageId(orphanageId);
    }


    @Override
    public void removeImage(String orphanageId, String imageId) {
        orphanageImageRepository.deleteByOrphanageIdAndId(orphanageId, imageId);
    }

    @Override
    public Optional<Orphanage> getOrphanageByEmail(String email) {
        return orphanageRepository.findByEmail(email);
    }

    @Override
    public String storeCertificate(String orpId, MultipartFile file) throws IOException {
        Optional<OrphanageDetails> optionalOrphanageDetails = orphanageDetailsRepository.findByOrpId(orpId);
        if (optionalOrphanageDetails.isPresent()) {
            OrphanageDetails orphanageDetails = optionalOrphanageDetails.get();
            orphanageDetails.setVerificationStatus(VerificationStatus.NOT_VERIFIED);
            orphanageDetails.setCertificate(file.getBytes());
            orphanageDetailsRepository.save(orphanageDetails);
            return "Certificate Uploaded Successfully";
        } else {
            OrphanageDetails orphanageDetails = new OrphanageDetails();
            if (validateCertificate(file)) {
                orphanageDetails.setCertificate(file.getBytes());
                orphanageDetails.setOrpId(orpId);
                orphanageDetails.setVerificationStatus(VerificationStatus.NOT_VERIFIED);
                orphanageDetailsRepository.save(orphanageDetails);
                return "Certificate Uploaded Successfully";
            } else {
                return "Invalid certificate format or size (optional error message)";
            }
        }
    }

    private boolean validateCertificate(MultipartFile file) {
        if (!Objects.requireNonNull(file.getContentType()).startsWith("application/pdf")) {
            return false;
        }
        if (file.getSize() > 1048576) {
            return false;
        }
        return true;
    }

    @Override
    public byte[] getCertificate(String orpId) {
        Optional<OrphanageDetails> optionalOrphanageDetails = orphanageDetailsRepository.findByOrpId(orpId);
        if (optionalOrphanageDetails.isPresent()) {
            OrphanageDetails orphanageDetails = optionalOrphanageDetails.get();
            byte[] certificateBytes = orphanageDetails.getCertificate();
            if (certificateBytes != null) {
                return certificateBytes;
            }
        }
        return null;
    }

    @Override
    public Optional<Orphanage> changeOrphanagePassword(String email, String oldPassword, String newPassword, String conformNewPassword) {
        Optional<Orphanage> orphanage=orphanageRepository.findByEmail(email);
        if(orphanage.isPresent()){
            if(newPassword.equals(conformNewPassword)){
                orphanage.get().setPassword(newPassword);
                orphanageRepository.save(orphanage.get());
                return orphanage;
            }
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public List<Donations> getDonationsByOrphanageId(String id) {
        return donationsRepo.getDonationsByOrpId(id);
    }

    @Override
    public Donor getDonorById(String donorId) {
        Optional<Donor> donor = donorRepository.findById(donorId);
        return donor.orElse(null);
    }

    @Override
    public Integer getViewCount(String orpId) {
        Optional<OrphanageDetails> orphanageDetails = orphanageDetailsRepository.findByOrpId(orpId);
        if(orphanageDetails.isPresent()){
            return orphanageDetails.get().getViewCount();
        }
        return 0;
    }

}
