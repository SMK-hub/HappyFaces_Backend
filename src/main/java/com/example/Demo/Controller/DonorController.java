package com.example.Demo.Controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.Demo.AdminServices.AdminService;
import com.example.Demo.Model.*;
import com.example.Demo.OrphanageServices.OrphanageService;
import com.example.Demo.RazorPayServices.RazorPayService;
import jdk.jfr.Event;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Demo.DonorServices.DonorService;
import com.example.Demo.Repository.DonorRepository;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/donor")
public class DonorController {

    @Autowired
    private DonorService donorService;
    @Autowired
    private OrphanageService orphanageService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private DonorRepository userRepo;

    public DonorController(RazorPayService razorPayService) {
        this.razorPayService = razorPayService;
    }

    @GetMapping
    public ResponseEntity<List<Donor>> getAll() {
        return new ResponseEntity<>(userRepo.findAll(),HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Donor user) {
        String alpha = donorService.registerUser(user);
        if (alpha.equals("Success")) {
            return new ResponseEntity<>("Registration Successful", HttpStatus.OK);
        }
        return new ResponseEntity<>("Existing User", HttpStatus.CONFLICT);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Map<String, String> loginData) {

        if (donorService.loginUser(loginData.get("email"), loginData.get("password"))) {
            return new ResponseEntity<>("Login successful!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Login Failed", HttpStatus.CONFLICT);
        }
    }

    private static final List<String> ALLOWED_IMAGE_CONTENT_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/bmp"
    );

    @PostMapping("/addPhoto/{donorId}")
    public ResponseEntity<Donor> addProfilePhoto(
            @PathVariable String donorId,
            @RequestParam("file") MultipartFile file) throws IOException {
        try {
            if (!isImage(file)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(donorService.addProfilePhoto(donorId, file));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private boolean isImage(MultipartFile file) {
        return file != null && ALLOWED_IMAGE_CONTENT_TYPES.contains(file.getContentType());
    }

    @GetMapping("/getPhoto/{donorId}")
    public ResponseEntity<byte[]> getProfilePhoto(@PathVariable String donorId) {
        String photoBase64 = donorService.getProfilePhoto(donorId);

        if (photoBase64 != null) {
            // Decode Base64 to byte array
            byte[] photoBytes = Base64.decodeBase64(photoBase64);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Set the appropriate content type

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(photoBytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/sendOtp")
    public ResponseEntity<String> sendOtp(@RequestBody Donor donor) {
        return new ResponseEntity<>(donorService.sendOtp(donor),HttpStatus.OK);

    }

    @PostMapping("/ForgetPassword/{email}/{otp}/{create}/{confirm}")
    public ResponseEntity<String> forgetPassword(@PathVariable("email") String email, @PathVariable("create") String create, @PathVariable("otp") String otp, @PathVariable("confirm") String confirm) {

        String alpha=donorService.forgetPassword(email, otp, create, confirm);
        if(alpha.equals("Password Changed Successfully")){
            return new ResponseEntity<>(alpha,HttpStatus.OK);
        }
        return new ResponseEntity<>(alpha,HttpStatus.CONFLICT);
    }
    @PostMapping("/ChangePassword/{email}/{oldPassword}/{newPassword}/{confirmNewPassword}")
    public ResponseEntity<Donor> changeDonorPassword(@PathVariable("email") String email,@PathVariable("oldPassword") String oldPassword,@PathVariable("newPassword") String newPassword,@PathVariable("confirmNewPassword") String confirmNewPassword){
        Donor alpha=donorService.changeDonorPassword(email,oldPassword,newPassword,confirmNewPassword);
        if(alpha!=null){
            return new ResponseEntity<>(alpha,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.CONFLICT);
    }

    @PutMapping("/{donorId}/editProfile")
    public ResponseEntity<Donor> editProfile(@PathVariable("donorId") String donorId, @RequestBody Donor donor) {
        Donor donorValue = donorService.editProfile(donorId, donor);
        if(donorValue!=null){
            return new ResponseEntity<>(donorValue,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.CONFLICT);
    }

    @GetMapping("/OrphanageDetails")
    public ResponseEntity<List<OrphanageDetails>> getVerifiedOrphanageDetails() {
        return ResponseEntity.ok(donorService.getVerifiedOrphanageDetails());
    }

    @PostMapping("/{orphanageId}/OrphanageDetails")
    public ResponseEntity<Optional<OrphanageDetails>> getVerifiedOrphanageDetailsById(@PathVariable("orphanageId") String orphanageId) {
        return ResponseEntity.ok(donorService.getVerifiedOrphanageDetailsById(orphanageId));
    }

    @GetMapping("/VerifiedEvents/{orphanageId}")
    public ResponseEntity<List<Events>> getVerifiedEvents(@PathVariable("orphanageId") String orphanageId) {
        return ResponseEntity.ok(donorService.getVerifiedEvents(orphanageId));
    }

    @PostMapping("/{donorId}/eventRegister/{eventId}")
    public ResponseEntity<String> eventRegister(@PathVariable("donorId") String donorId, @PathVariable("eventId") String eventId) {
        String alpha=donorService.eventRegister(eventId, donorId);
        if(alpha.equals("Event Registration is Done")){
        return new ResponseEntity<>("Registration Successfully Done", HttpStatus.OK);
        }
        return new ResponseEntity<>(alpha,HttpStatus.CONFLICT);
    }

    @PostMapping("/{donorId}/cancelEventRegister/{eventId}")
    public ResponseEntity<String> cancelEventRegister(@PathVariable("donorId") String donorId, @PathVariable("eventId") String eventId) {
        String alpha=donorService.cancelEventRegistration(eventId, donorId);
        if(alpha.equals("Event Registration Cancelled")) {
            return new ResponseEntity<>(alpha, HttpStatus.OK);
        }
        return new ResponseEntity<>(alpha,HttpStatus.CONFLICT);
    }

    @GetMapping("/donor/{donorEmail}")
    public ResponseEntity<Donor> getDonorByEmail(@PathVariable String donorEmail){
        Optional<Donor> donor=donorService.getDonorByEmail(donorEmail);
        return donor.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    @GetMapping("/orphanageImages/{orphanageId}")
    public ResponseEntity<List<OrphanageImage>> getOrphanageImageById(@PathVariable String orphanageId){
        List<OrphanageImage> images=orphanageService.getOrphanageImagesById(orphanageId);
        if((long) images.size() > 0){
            return new ResponseEntity<>(images,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.CONFLICT);
    }
    @GetMapping("/DonationList/{donorId}")
    public ResponseEntity<List<Donations>> getDonationById(@PathVariable String donorId){
        List<Donations> donations=adminService.getDonationsByDonorId(donorId);
        if((long) donations.size() > 0){
            return new ResponseEntity<>(donations,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.CONFLICT);
    }
    @PostMapping("/donationData")
    public ResponseEntity<Donations> saveDonationData(@RequestBody Donations donations){
        Donations donations1 = donorService.saveDonationDetail(donations);
        return new ResponseEntity<>(donations1,HttpStatus.OK);
    }
    private RazorPayService razorPayService;

    public void RazorPayController(RazorPayService razorPayService) {
        this.razorPayService = razorPayService;
    }

    @PostMapping("/generateOrder")
    public ResponseEntity<String> generateOrder(@RequestBody Donations donations) {
        try {
            String orderId = razorPayService.createOrder(donations);
            return new ResponseEntity<>(orderId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/participatedDonorsId/{eventId}")
    public ResponseEntity<List<String>> getAllParticipatedDonorsId(@PathVariable String eventId) {
        return new ResponseEntity<>(donorService.getDonorIdFromEvent(eventId),HttpStatus.OK);
    }

    @PostMapping("/save/DonationRequirement")
    public ResponseEntity<String> saveDonationRequirement(@RequestBody DonationRequirements donationRequirements){
        return new ResponseEntity<>(donorService.saveDonationRequirements(donationRequirements),HttpStatus.OK);
    }
    @GetMapping("{donorId}/DonationRequirement")
    public ResponseEntity<List<DonationRequirements>> getDonationRequirementByDonorId(@PathVariable String donorId){
        return new ResponseEntity<>(donorService.getDonationRequirementsByDonorId(donorId),HttpStatus.OK);
    }
    @GetMapping("/RegisteredEvents/{donorId}")
    public ResponseEntity<List<InterestedPerson>> getAllInterestedPersonByDonorId(@PathVariable String donorId){
        return new ResponseEntity<>(donorService.findAllInterestedPersonByDonorId(donorId),HttpStatus.OK);
    }
    @GetMapping("/Event/{eventId}")
    public ResponseEntity<Events> getEventByEventId(@PathVariable String eventId){
        return new ResponseEntity<>(donorService.findEventByEventId(eventId),HttpStatus.OK);
    }
    @PutMapping("/addViewCount/{orpId}")
    public ResponseEntity<String> addOrphanageViewCount(@PathVariable String orpId){
        donorService.increaseViewCount(orpId);
        return new ResponseEntity<>("View count Increased",HttpStatus.OK);
    }
}
