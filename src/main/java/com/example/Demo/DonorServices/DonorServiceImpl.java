package com.example.Demo.DonorServices;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.example.Demo.Enum.EnumClass;
import com.example.Demo.Model.*;
import com.example.Demo.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Demo.EmailServices.EmailService;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DonorServiceImpl implements DonorService {

    @Autowired
    private DonorRepository donorRepository;
    @Autowired
    private OrphanageDetailsRepository orphanageDetailsRepository;
    @Autowired
    private DonationRequirementRepository donationRequirementRepository;
    @Autowired
    private InterestedPersonRepository interestedPersonRepository;
    @Autowired
    private OrphanageRepository orphanageRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EventsRepository eventsRepository;
    @Autowired
    private OrphanageDetailsRepository detailsRepository;
    @Autowired
    private DonationsRepository donationsRepository;

    public void saveUser(Optional<Donor> optionalDonor) {
        optionalDonor.ifPresent(donor -> {
            donorRepository.save(donor);
        });
    }

    public String registerUser(Donor newUser) {
        Optional<Donor> user = donorRepository.findByEmail(newUser.getEmail());
        Optional<Orphanage> orpUser=orphanageRepository.findByEmail(newUser.getEmail());
        Optional<Admin> adminUser=adminRepository.findByEmail(newUser.getEmail());

        if (user.isEmpty() && orpUser.isEmpty() && adminUser.isEmpty()) {
            newUser.setRole(EnumClass.Roles.valueOf(String.valueOf(EnumClass.Roles.DONOR)));
            donorRepository.save(newUser);
            String subject = "Registration Successful";
            String body = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "  <meta charset=\"UTF-8\">\n" +
                    "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "  <title>Welcome to Happy Faces!</title>\n" +
                    "  <style>\n" +
                    "    body {\n" +
                    "      font-family: Arial, sans-serif;\n" +
                    "      margin: 0;\n" +
                    "      padding: 20px;\n" +
                    "      background-color: #f9f9f9;\n" +
                    "    }\n" +
                    "    .container {\n" +
                    "      max-width: 600px;\n" +
                    "      margin: 0 auto;\n" +
                    "      background-color: #fff;\n" +
                    "      padding: 30px;\n" +
                    "      border-radius: 8px;\n" +
                    "      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                    "    }\n" +
                    "    h1 {\n" +
                    "      font-size: 28px;\n" +
                    "      color: #333;\n" +
                    "      margin-bottom: 20px;\n" +
                    "    }\n" +
                    "    p {\n" +
                    "      line-height: 1.6;\n" +
                    "      font-size: 16px;\n" +
                    "      color: #555;\n" +
                    "      margin-bottom: 15px;\n" +
                    "    }\n" +
                    "    .signature {\n" +
                    "      margin-top: 30px;\n" +
                    "      font-style: italic;\n" +
                    "      color: #888;\n" +
                    "    }\n" +
                    "  </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "  <div class=\"container\">\n" +
                    "    <h1>Welcome to Happy Faces, "+newUser.getName()+"!</h1>\n" +
                    "    <p>We are thrilled to welcome you to our community of generous donors!</p>\n" +
                    "    <p>Your registration as a donor brings smiles to countless faces. Thank you for joining us in making a positive impact on the lives of children in need.</p>\n" +
                    "    <p>We are committed to providing essential support and resources to children through your kind contributions. Together, we can make a real difference.</p>\n" +
                    "    <p>We encourage you to explore our website to learn more about our mission, programs, and impact stories.</p>\n" +
                    "    <p>Thank you again for joining us, "+newUser.getName()+"! We look forward to your continued support.</p>\n" +
                    "    <p class=\"signature\">Sincerely,<br>The Happy Faces Team</p>\n" +
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
        Optional<Donor> user = donorRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get().getPassword().equals(password);
        } else {
            return false;
        }
    }

    @Override
    public Optional<Donor> viewProfile(String donorId) {
        return donorRepository.findById(donorId);
    }

    @Override
    public Donor editProfile(String donorId, Donor donor) {
        Optional<Donor> optionalDonor = donorRepository.findById(donorId);

        if (optionalDonor.isPresent()) {
            Donor existingDonor = optionalDonor.get();

            Class<?> donorClass = Donor.class;
            Field[] fields = donorClass.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                try {
                    Object newValue = field.get(donor);
                    if (newValue != null && !newValue.toString().isEmpty()) {
                        field.set(existingDonor, newValue);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // Handle the exception according to your needs
                }
            }

            donorRepository.save(existingDonor);
            return existingDonor;
        }

        return null;
    }

    @Override
    public Donor addProfilePhoto(String donorId, MultipartFile file) throws IOException {
        byte[] photoBytes = file.getBytes();


        Donor donor= donorRepository.findById(donorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        donor.setProfilePhoto(photoBytes);
        donorRepository.save(donor);
        return donor;
    }
    @Override
    public String getProfilePhoto(String donorId) {
        Donor donor= donorRepository.findById(donorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        byte[] photoBytes = donor.getProfilePhoto();
        if (photoBytes != null && photoBytes.length > 0) {
            return Base64.getEncoder().encodeToString(photoBytes);
        } else {
            return null; // Or return a default image URL or handle it based on your requirements
        }
    }

    @Override
    public String sendOtp(Donor donor) {

        Optional<Donor> user = donorRepository.findByEmail(donor.getEmail());
//		if (saved.isPresent()) {
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

            Otp=sixDigitCode;
            String res = emailService.sendHtmlMail(user.get().getEmail(), subject, body);

            return sixDigitCode;
        }
        return null;
    }
    private String Otp;
    @Override
    public String forgetPassword(String email, String otp, String create, String confirm) {
        Optional<Donor> user = donorRepository.findByEmail(email);
//	if(saved.isPresent()) {


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

    @Override
    public List<OrphanageDetails> getVerifiedOrphanageDetails() {
        return detailsRepository.findByVerificationStatus(String.valueOf(EnumClass.VerificationStatus.VERIFIED));
    }

    @Override
    public Optional<OrphanageDetails> getVerifiedOrphanageDetailsById(String orpId) {
        Optional<OrphanageDetails> optionalOrphanageDetails=detailsRepository.findByOrpId(orpId);
        if(optionalOrphanageDetails.isPresent() && optionalOrphanageDetails.get().getVerificationStatus().equals(EnumClass.VerificationStatus.VERIFIED)){
            return optionalOrphanageDetails;
        }else {
            return Optional.empty();
        }
    }

    @Override
    public List<Events> getVerifiedEvents(String orpId) {
        return eventsRepository.findByVerificationStatusAndOrpId(String.valueOf(EnumClass.VerificationStatus.VERIFIED),orpId);
    }

    @Override
    public String eventRegister(String eventId, String donorId) {
        Optional<Events> event = eventsRepository.findById(eventId);
        Optional<Donor> donor = donorRepository.findById(donorId);
        List<InterestedPerson> donorParticipating = interestedPersonRepository.findAllInterestedPersonByDonorId(donorId);
        Optional<InterestedPerson> checkingEvent= donorParticipating.stream().filter(donorParticipating1->donorParticipating1.getEventId().equals(eventId)).findAny();

        if (event.isPresent() && donor.isPresent() && checkingEvent.isEmpty()) {
            InterestedPerson interestedPerson = new InterestedPerson(); // Initialize the object

            interestedPerson.setDonorId(donor.get().getDonorId());
            interestedPerson.setContact(donor.get().getContact());
            interestedPerson.setEmail(donor.get().getEmail());
            interestedPerson.setName(donor.get().getName());
            interestedPerson.setEventId(event.get().getId());

            interestedPersonRepository.save(interestedPerson);
            Optional<OrphanageDetails> orphanageDetails = orphanageDetailsRepository.findByOrpId(event.get().getOrpId());
            String subject = "Event Registration is Done";
            if(orphanageDetails.isPresent()) {
                String body = generateEventRegistrationConfirmation(donor.get(), event.get(), orphanageDetails.get());
                emailService.sendHtmlMail(donor.get().getEmail(), subject, body);
            }
            return subject;
        }
        return "Problem in Event Registration Process";
    }

    private String generateEventRegistrationConfirmation(Donor donor, Events event, OrphanageDetails orphanageDetails) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>Event Registration Confirmation</title>\n" +
                "  <style>\n" +
                "    body {\n" +
                "      font-family: sans-serif;\n" +
                "      margin: 0;\n" +
                "      padding: 20px;\n" +
                "    }\n" +
                "    h2 {\n" +
                "      font-size: 20px;\n" +
                "      color: #333;\n" +
                "      margin-bottom: 10px;\n" +
                "    }\n" +
                "    p {\n" +
                "      line-height: 1.5;\n" +
                "      font-size: 16px;\n" +
                "      color: #333;\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <h2>Event Registration Confirmation</h2>\n" +
                "  <p>Dear " + donor.getName() + ",</p>\n" +
                "  <p>We are thrilled to confirm your registration for our upcoming event, \"" + event.getTitle() + "\".</p>\n" +
                "  <p>Your generosity in registering for this event is a vital contribution to our cause. We appreciate your support and look forward to seeing you there!</p>\n" +
                "  <p><strong>Event Details:</strong></p>\n" +
                "  <ul>\n" +
                "    <li><strong>Event Title:</strong> " + event.getTitle() + "</li>\n" +
                "    <li><strong>Date:</strong> " + event.getDate() + "</li>\n" +
                "    <li><strong>Time:</strong> " + event.getTime() + "</li>\n" +
                "    <li><strong>Location:</strong> " + orphanageDetails.getAddress().getHouse_no() +","+orphanageDetails.getAddress().getStreet()+ ","+orphanageDetails.getAddress().getCity()+","+orphanageDetails.getAddress().getCountry()+"-"+orphanageDetails.getAddress().getCountry()+"</li>\n" +
                "  </ul>\n" +
                "  <p>If you have any questions or require further information, please don't hesitate to contact orphanage at "+ orphanageDetails.getContact()+".</p>\n" +
                "  <p>Sincerely,</p>\n" +
                "  <p>Happy Faces</p>\n" +
                "</body>\n" +
                "</html>";

    }

    @Override
    public String cancelEventRegistration(String eventId, String donorId) {
        List<InterestedPerson> donorParticipating = interestedPersonRepository.findAllInterestedPersonByDonorId(donorId);
        Optional<InterestedPerson> cancellingEvent = donorParticipating.stream().filter(donorParticipating1->donorParticipating1.getEventId().equals(eventId)).findAny();
        if(cancellingEvent.isPresent()) {
            String donorEmail=cancellingEvent.get().getEmail();
            Optional<Donor> donor = donorRepository.findByEmail(donorEmail);
            Optional<Events> events = eventsRepository.findById(cancellingEvent.get().getEventId());
            interestedPersonRepository.delete(cancellingEvent.get());
            String subject = "Event Registration Cancelled";
            String body =
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "  <meta charset=\"UTF-8\">\n" +
                    "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "  <title>Event Cancellation Notification</title>\n" +
                    "  <style>\n" +
                    "    body {\n" +
                    "      font-family: sans-serif;\n" +
                    "      margin: 0;\n" +
                    "      padding: 20px;\n" +
                    "    }\n" +
                    "    p {\n" +
                    "      line-height: 1.5;\n" +
                    "      font-size: 16px;\n" +
                    "      color: #333;\n" +
                    "    }\n" +
                    "    .header {\n" +
                    "      font-weight: bold;\n" +
                    "      margin-bottom: 10px;\n" +
                    "    }\n" +
                    "  </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "  <h2 class=\"header\">Event Cancellation Notification</h2>\n" +
                    "  <p>Dear "+donor.get().getName()+",</p>\n" +
                    "  <p>We received your notification regarding the cancellation of your participation in the "+ events.get().getTitle() +" event. While we understand that circumstances may change, we appreciate you letting us know in advance.</p>\n" +
                    "  <p>We appreciate your initial commitment to the event, and we hope to welcome you back as a valued participant in future endeavors.</p>\n" +
                    "  <p>Sincerely,</p>\n" +
                    "  <p>Happy Faces</p>\n" +
                    "</body>\n" +
                    "</html>\n";
            emailService.sendHtmlMail(donorEmail, subject, body);

            return subject;
        }
        return "Unable to Cancel the registration";
    }

    @Override
    public Optional<Donor> getDonorByEmail(String email) {
        return donorRepository.findByEmail(email);
    }

    @Override
    public Donor changeDonorPassword(String email,String oldPassword, String newPassword, String confirmNewPassword) {
        Optional<Donor> donor=donorRepository.findByEmail(email);
        if(donor.isPresent()){
            if(newPassword.equals(confirmNewPassword)){
                donor.get().setPassword(newPassword);
                donorRepository.save(donor.get());
                return donor.get();
            }
            return null;
        }
        return null;
    }

    @Override
    public Donations saveDonationDetail(Donations donations) {
        String orphanageId = donations.getOrpId();
        String donorId = donations.getDonorId();
        Optional<Orphanage> orphanage = orphanageRepository.findById(orphanageId);
        Optional<OrphanageDetails> orphanageDetails = orphanageDetailsRepository.findByOrpId(orphanageId);
        Optional<Donor> donor = donorRepository.findById(donorId);
        if(orphanageDetails.isPresent() && donor.isPresent() && orphanage.isPresent()){
            String orphanageEmail = orphanage.get().getEmail();
            String donorEmail = donor.get().getEmail();
            String subject = "Donation Receipt";
            String body = generateDonorReceiptHTML(donor.get().getName(),orphanageDetails.get().getOrphanageName(),donations.getAmount(),donations.getDateTime(),donations.getTransactionId());
            emailService.sendHtmlMail(donorEmail,subject,body);
        }
        donationsRepository.save(donations);
        return donationsRepository.findById(donations.getId()).orElse(null);
    }

    private String generateDonorReceiptHTML(String donorName,String orpName,String amount,String dateTime,String transactionId) {

        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>Donation Receipt</title>\n" +
                "  <style>\n" +
                "    body {\n" +
                "      font-family: sans-serif;\n" +
                "      margin: 0;\n" +
                "      padding: 20px;\n" +
                "    }\n" +
                "    h1 {\n" +
                "      text-align: center;\n" +
                "      font-size: 24px;\n" +
                "      margin-bottom: 20px;\n" +
                "    }\n" +
                "    table {\n" +
                "      width: 100%;\n" +
                "      border-collapse: collapse;\n" +
                "      margin-bottom: 20px;\n" +
                "      border: 1px solid #ddd; /* Add a border around the entire table */\n" +
                "    }\n" +
                "    th, td {\n" +
                "      padding: 10px;\n" +
                "      border: 1px solid #ddd; /* Add borders to individual cells */\n" +
                "    }\n" +
                "    th {\n" +
                "      background-color: #f2f2f2; /* Add a light gray background to table headers */\n" +
                "      text-align: left; /* Align table headers to the left */\n" +
                "    }\n" +
                "    .contact-info {\n" +
                "      text-align: center;\n" +
                "      margin-top: 20px;\n" +
                "    }\n" +
                "    .logo {\n" +
                "      width: 100px;\n" +
                "      height: 100px;\n" +
                "      float: left;\n" +
                "      margin-right: 20px;\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div style=\"display: flex; align-items: center;\">\n" +
                "    <h1>Donation Receipt</h1>\n" +
                "  </div>\n" +
                "  <table>\n" +
                "    <tr>\n" +
                "      <th style=\"width: 50%\">Transaction Id:</th>\n" +
                "      <td>" + transactionId+ "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <th>Date:</th>\n" +
                "      <td>" + dateTime + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <th>Donor Name:</th>\n" +
                "      <td>" + donorName + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <th>Recipient:</th>\n" +
                "      <td>" + orpName + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <th>Amount Donated:</th>\n" +
                "      <td> Rs." + amount + "</td>\n" +
                "    </tr>\n" +
                "  </table>\n" +
                "  <p style=\"text-align: center;\">Thank you for your generous donation! Your support will make a difference in the lives of the children at " + orpName + "</p>\n" +
                "  <p class=\"contact-info\">\n" +
                "    Sincerely,<br>\n" +
                "    The Happy Faces Team<br>\n" +
                "    <a href=\"mailto:happyfacesweb@gmail.com\">happyfacesweb@gmail.com</a><br>\n" +
                "    (555) 555-5555\n" +
                "  </p>\n" +
                "</body>\n" +
                "</html>";
    }

    @Override
    public List<String> getDonorIdFromEvent(String eventId) {
        List<InterestedPerson> interestedPerson=interestedPersonRepository.findAllInterestedPersonByEventId(eventId);
        return interestedPerson.stream().map(InterestedPerson::getDonorId).toList();
    }

    @Override
    public String saveDonationRequirements(DonationRequirements donationRequirements) {
        donationRequirementRepository.save(donationRequirements);
        Optional<Donor> donor = donorRepository.findById(donationRequirements.getDonorId());
        Optional<OrphanageDetails> orphanageDetails=orphanageDetailsRepository.findByOrpId(donationRequirements.getOrpId());
        Optional<Orphanage> orphanage= orphanageRepository.findById(donationRequirements.getOrpId());
        String subject="Heartfelt Thanks for Your Generous Donation";
        String body="<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>Donation Confirmation</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <p>Dear "+ donor.get().getName() +",</p>\n" +
                "  <p>Thank you for your generous donation to \" "+ orphanageDetails.get().getOrphanageName() +"\".</p>\n" +
                "  <p>Your kindness will make a lasting impact, providing hope and support to the children in our care.</p>\n" +
                "  <p>We are deeply grateful for your compassion and generosity.</p>\n" +
                "</body>\n" +
                "</html>";
        String subjectOrp = "Generous Donation Offer from "+donor.get().getName() +"\uD83D\uDE0A";
        String bodyOrp = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>Donation Offer Confirmation</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <p>Dear "+ orphanageDetails.get().getOrphanageName() +",</p>\n" +
                "  <p>We are thrilled to inform you that  <strong>"+ donor.get().getName() +"</strong> has graciously offered to donate essential items to support the children in your care.</p>\n" +
                "  <p>In their own words: \""+ donationRequirements.getDescription() +"\".</p>\n" +
                "  <p>We are excited to coordinate this contribution to enhance the lives of the children at your orphanage.</p>\n" +
                "</body>\n" +
                "</html>";
        emailService.sendHtmlMail(donor.get().getEmail(),subject,body);
        emailService.sendHtmlMail(orphanage.get().getEmail(),subjectOrp,bodyOrp);
        return "Requirement Donation Info Saved Successfully";
    }

    @Override
    public List<DonationRequirements> getDonationRequirementsByDonorId(String donorId) {
        return donationRequirementRepository.findAllDonationsRequirementByDonorId(donorId);
    }

    @Override
    public List<DonationRequirements> getAllDonationRequirementByDonorId(String donorId) {
        Optional<Donor> donor = donorRepository.findById(donorId);
        if(donor.isPresent()){
            return donationRequirementRepository.findAllDonationsRequirementByDonorId(donorId);
        }
        return null;
    }

    @Override
    public List<InterestedPerson> findAllInterestedPersonByDonorId(String donorId) {
        return interestedPersonRepository.findAllInterestedPersonByDonorId(donorId);
    }

    @Override
    public Events findEventByEventId(String eventId) {
        Optional<Events> events = eventsRepository.findById(eventId);
        if(events.isPresent()){
            return events.get();
        }
        return null;
    }

    @Override
    public void increaseViewCount(String orpId) {
        Optional<OrphanageDetails> orphanageDetails = orphanageDetailsRepository.findByOrpId(orpId);
        orphanageDetails.ifPresent(details -> details.setViewCount(details.getViewCount() + 1));
    }
}
