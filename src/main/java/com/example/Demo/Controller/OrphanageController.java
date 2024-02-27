package com.example.Demo.Controller;

import java.io.IOException;
import java.util.*;

import com.example.Demo.AdminServices.AdminService;
import com.example.Demo.Model.*;
import com.example.Demo.Repository.OrphanageImageRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Demo.OrphanageServices.OrphanageService;
import com.example.Demo.Repository.OrphanageRepository;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/orphanage")
public class OrphanageController {

    @Autowired
    private OrphanageService orphanageService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private OrphanageRepository orphanageRepo;

    @GetMapping
    public ResponseEntity<List<Orphanage>> getAll() {
        return new ResponseEntity<>(orphanageRepo.findAll(),HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Orphanage user) {
        String alpha =  orphanageService.registerUser(user);
        if(alpha.equals("Success")){
            return new ResponseEntity<>(alpha,HttpStatus.OK);
        }
        return new ResponseEntity<>(alpha,HttpStatus.CONFLICT);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Map<String, String> loginData) {

        if (orphanageService.loginUser(loginData.get("email"), loginData.get("password"))) {
            return new ResponseEntity<>("Login successful!",HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid email or password",HttpStatus.CONFLICT);
        }
    }
    private static final List<String> ALLOWED_IMAGE_CONTENT_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/bmp"
    );
    @PostMapping("addPhoto/{orphanageId}")
    public ResponseEntity<String> addProfilePhoto(
            @PathVariable String orphanageId,
            @RequestParam("file") MultipartFile file) throws IOException {
        try {
            // Check if the uploaded file is an image
            if (!isImage(file)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only image files are allowed");
            }
            orphanageService.addProfilePhoto(orphanageId, file);
            return ResponseEntity.status(HttpStatus.CREATED).body("Profile photo added successfully");
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding profile photo");
        }
    }
    private boolean isImage(MultipartFile file) {
        return file != null && ALLOWED_IMAGE_CONTENT_TYPES.contains(file.getContentType());
    }
    @GetMapping("getPhoto/{orphanageId}")
    public ResponseEntity<byte[]> getProfilePhoto(@PathVariable String orphanageId) {
        String photoBase64 = orphanageService.getProfilePhoto(orphanageId);

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
    @PutMapping("updatePhoto/{orphanageId}")
    public ResponseEntity<String> updateProfilePhoto(
            @PathVariable String orphanageId,
            @RequestParam("file") MultipartFile file) {
        try {
            if (!isImage(file)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only image files are allowed");
            }
            orphanageService.updateProfilePhoto(orphanageId, file);
            return ResponseEntity.status(HttpStatus.CREATED).body("Profile photo updated successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating profile photo");
        }
    }

    @PostMapping("/enterDetails")
    public ResponseEntity<String> updateDetails(@RequestBody OrphanageDetails detail) {
        return new ResponseEntity<>(orphanageService.updateDetails(detail),HttpStatus.OK);

    }
    @PostMapping("/createEvent")
    public ResponseEntity<String> createEvents(@RequestBody Events event){
        String alpha = orphanageService.createEvents(event);
        if(alpha.equals("Event Created")){
            return new ResponseEntity<>(alpha,HttpStatus.OK);
        }
        return new ResponseEntity<>("Internal Error",HttpStatus.CONFLICT);
    }

    @GetMapping("/plannedEvents/{orpId}")
    public ResponseEntity<List<Events>> getPlannedEvents(@PathVariable String orpId){
        List<Events> alpha = orphanageService.getPlannedEvents(orpId);
        if(alpha!=null){
            return new ResponseEntity<>(alpha,HttpStatus.OK);
        }
        List<Events> emptyList = new ArrayList<>();
        return new ResponseEntity<>(emptyList,HttpStatus.NOT_FOUND);
    }

    @PostMapping("/cancelEvent/{eventId}")
    public ResponseEntity<String> cancelEvents(@PathVariable String eventId){
        String alpha = orphanageService.cancelEvent(eventId);
        if(alpha.equals("Event Cancelled Successfully")) {
            return new ResponseEntity<>(alpha, HttpStatus.OK);
        }
        return new ResponseEntity<>(alpha,HttpStatus.CONFLICT);
    }

    @PutMapping("/editEvents/{eventId}")
    public ResponseEntity<String> editEvents(@PathVariable String eventId,@RequestBody Events event){
        String alpha = orphanageService.editEvent(eventId,event);
        if(alpha.equals("Event Updated Successfully")){
            return new ResponseEntity<>(alpha,HttpStatus.OK);
        }
        return new ResponseEntity<>(alpha,HttpStatus.CONFLICT);
    }
    @GetMapping("/getInterestedPerson/{eventId}")
    public  ResponseEntity<List<InterestedPerson>> getInterestedPersonByEventId(@PathVariable String eventId){
        List<InterestedPerson> interestedPersonList = orphanageService.getInterestedPersonByEventId(eventId);
        return new ResponseEntity<>(interestedPersonList,HttpStatus.OK);
    }
    @PostMapping("/sendOtp")
    public ResponseEntity<String> sendOtp(@RequestBody Orphanage orphanage) {
        String alpha = orphanageService.sendOtp(orphanage);
        if(alpha!=null){
            return new ResponseEntity<>(alpha,HttpStatus.OK);
        }
        return new ResponseEntity<>("Internal Error",HttpStatus.CONFLICT);
    }
    @PostMapping("/ForgetPassword/{email}/{otp}/{create}/{confirm}")
    public ResponseEntity<String> forgetPassword(@PathVariable("email") String email,@PathVariable("create") String create,@PathVariable("otp") String otp,@PathVariable("confirm") String confirm) {
        System.out.println(create + "   " + confirm + "  " + otp);
        String alpha=orphanageService.forgetPassword(email,otp,create,confirm);
       if(alpha.equals("Password Changed Successfully")){
           return new ResponseEntity<>(alpha,HttpStatus.OK);
       }
       return new ResponseEntity<>(alpha,HttpStatus.CONFLICT);
    }
    @PostMapping("/ChangePassword/{email}/{oldPassword}/{newPassword}/{confirmNewPassword}")
    public ResponseEntity<Optional<Orphanage>> changeOrphanagePassword(@PathVariable("email") String email,@PathVariable("oldPassword") String oldPassword,@PathVariable("newPassword") String newPassword,@PathVariable("confirmNewPassword") String confirmNewPassword){
        Optional<Orphanage> alpha=orphanageService.changeOrphanagePassword(email,oldPassword,newPassword,confirmNewPassword);
        if(alpha.isPresent()){
            return new ResponseEntity<>(alpha,HttpStatus.OK);
        }
        return new ResponseEntity<>(alpha,HttpStatus.CONFLICT);
    }
    @PutMapping("/{orphanageId}/editProfile")
    public ResponseEntity<Orphanage> editProfile(@PathVariable("orphanageId") String orphanageId,@RequestBody Orphanage orphanage){
        Orphanage orphanageValue= orphanageService.editProfile(orphanageId,orphanage);
        if(orphanageValue!=null){
            return new ResponseEntity<>(orphanageValue,HttpStatus.OK);
        }
        return new ResponseEntity<>(orphanageValue,HttpStatus.CONFLICT);
    }

    @GetMapping("/{orphanageId}/details")
    public ResponseEntity<Optional<OrphanageDetails>> getDetailsById(@PathVariable("orphanageId") String orphanageId){
        Optional<OrphanageDetails> orphanageDetails = orphanageService.getDetailById(orphanageId);
        if(orphanageDetails.isPresent()){
            return new ResponseEntity<>(orphanageDetails,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.CONFLICT);
    }
    @PostMapping("/{orphanageId}/editDetails")
    public ResponseEntity<String> editDetails(@PathVariable("orphanageId") String orphanageId,@RequestBody OrphanageDetails orphanageDetails){
        String alpha = orphanageService.editDetails(orphanageId,orphanageDetails);
        if(alpha.equals("Details Updated Successfully")){
            return new ResponseEntity<>(alpha,HttpStatus.OK);
        }
        return new ResponseEntity<>(alpha,HttpStatus.CONFLICT);
    }
    @PostMapping("/{orphanageId}/orphanageDetails/uploadImages")
    public ResponseEntity<String> uploadImagesById(@PathVariable("orphanageId")String orphanageId,@RequestParam("images") List<MultipartFile> images) throws IOException {
        orphanageService.uploadImages(orphanageId, images);
        return ResponseEntity.ok("Images uploaded successfully for orphanageId: " + orphanageId);
    }
    @GetMapping("{orphanageId}/orphanageDetails/viewImages")
    public ResponseEntity<List<OrphanageImage>> getImagesByOrphanageId(@PathVariable String orphanageId) {
        List<OrphanageImage> images = orphanageService.getOrphanageImagesById(orphanageId);
        return new ResponseEntity<>(images,HttpStatus.OK);
    }
    @PostMapping("{orphanageId}/orphanageDetails/removeImage/{imageId}")
    public ResponseEntity<String> removeImageById(@PathVariable("orphanageId")String orphanageId,@PathVariable("imageId") String imageId){
        orphanageService.removeImage(orphanageId,imageId);
        return ResponseEntity.ok("Image Removed Successfully");
    }
    @GetMapping("/orphanage/{orphanageEmail}")
    public ResponseEntity<Orphanage> getDonorByEmail(@PathVariable String orphanageEmail){
        Optional<Orphanage> orphanage=orphanageService.getOrphanageByEmail(orphanageEmail);
        return orphanage.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    @GetMapping("/DonationList/{orphanageId}")
    public ResponseEntity<List<Donations>> getDonationById(@PathVariable String orphanageId){
        List<Donations> donations=adminService.getDonationsByOrphanageId(orphanageId);
        if((long) donations.size() > 0){
            return new ResponseEntity<>(donations,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.CONFLICT);
    }
    @PostMapping("/uploadCertificate/{orphanageId}")
    public ResponseEntity<String> uploadCertificate(@PathVariable String orphanageId,@RequestParam("file") MultipartFile file) {
        try {
            String response = orphanageService.storeCertificate(orphanageId,file);
            if(response.equals("Certificate Uploaded Successfully")) {
                return new ResponseEntity<>(response,HttpStatus.OK);
            }
            return new ResponseEntity<>(null,HttpStatus.CONFLICT);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload certificate: " + e.getMessage());
        }
    }
    @GetMapping("/getCertificate/{orpId}")
    public ResponseEntity<byte[]> getCertificate(@PathVariable String orpId) {
        byte[] certificateBytes = orphanageService.getCertificate(orpId);
        if (certificateBytes != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "certificate.pdf");
            headers.setContentLength(certificateBytes.length);
            return new ResponseEntity<>(certificateBytes, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/donation/{orpId}")
    public ResponseEntity<List<Donations>> getDonationsByOrphanageId(@PathVariable String orpId){
        return new ResponseEntity<>(orphanageService.getDonationsByOrphanageId(orpId),HttpStatus.OK);
    }
    @GetMapping("donor/{donorId}")
    public ResponseEntity<Donor> getDonorById(@PathVariable String donorId){
        return new ResponseEntity<>(orphanageService.getDonorById(donorId),HttpStatus.OK);
    }

    @GetMapping("/viewCount/{orpId}")
    public ResponseEntity<Integer> getViewCountById(@PathVariable String orpId){
        return new ResponseEntity<>(orphanageService.getViewCount(orpId),HttpStatus.OK);
    }
}
