package com.example.Demo.Controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.Demo.Enum.EnumClass;
import com.example.Demo.Model.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Demo.AdminServices.AdminService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@GetMapping("/adminList")
	public ResponseEntity<List<Admin>> getAllAdmin() {
		return new ResponseEntity<>(adminService.getAllAdmin(),HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Admin> getAdminById(@PathVariable String id){
		Optional<Admin> admin = Optional.ofNullable(adminService.getAdminById(id));
		return admin.map(value -> new ResponseEntity<>(value,HttpStatus.OK)).orElseGet(()->new ResponseEntity<>(null, HttpStatus.CONFLICT));
	}

	@GetMapping("/orphanageList")
	public ResponseEntity<List<Orphanage>> getAllOrphanage() {
		return new ResponseEntity<>(adminService.getAllOrphanage(),HttpStatus.OK);
	}
	@GetMapping("/orphanage/{id}")
	public ResponseEntity<Orphanage> getOrphanageById(@PathVariable String id) {
		Optional<Orphanage> orphanage = Optional.ofNullable(adminService.getOrphanageById(id));
		return orphanage.map(value -> new ResponseEntity<>(value,HttpStatus.OK)).orElseGet(()->new ResponseEntity<>(null, HttpStatus.CONFLICT));
	}
	@GetMapping("/orphanageDetailsList")
	public ResponseEntity<List<OrphanageDetails>> getAllOrphanageDetails(){
		return new ResponseEntity<>(adminService.getAllOrphanageDetails(),HttpStatus.OK);
	}
	@GetMapping("/orphanageDetails/{orpId}")
	public ResponseEntity<OrphanageDetails> getOrphanageDetailByOrpId(@PathVariable String orpId)
	{
		Optional<OrphanageDetails> orphanageDetails = Optional.ofNullable(adminService.getOrphanageDetailByOrphanageId(orpId));
		return orphanageDetails.map(value -> new ResponseEntity<>(value,HttpStatus.OK)).orElseGet(()->new ResponseEntity<>(null, HttpStatus.CONFLICT));
	}
	@GetMapping("/donorList")
	public ResponseEntity<List<Donor>> getAllDonor() {
		return new ResponseEntity<>(adminService.getAllDonor(),HttpStatus.OK);
	}

	@GetMapping("/donor/{id}")
	public ResponseEntity<Donor> getDonorById(@PathVariable String id){
		Optional<Donor> donor=Optional.ofNullable(adminService.getDonorById(id));
		return donor.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.CONFLICT));
	}

	@GetMapping("/eventList")
	public ResponseEntity<List<Events>> getAllEvents() {
		return new ResponseEntity<>(adminService.getAllEvents(),HttpStatus.OK);
	}

	@GetMapping("/event/{id}")
	public ResponseEntity<Events> getEventById(@PathVariable String id){
		Optional<Events> events = Optional.ofNullable(adminService.getEventById(id));
		return events.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.CONFLICT));
	}

	@GetMapping("/event/orphanageId/{id}")
	public ResponseEntity<List<Events>> getEventsByOrphanageId(@PathVariable String id){
		return new ResponseEntity<>(adminService.getEventsByOrphanageId(id),HttpStatus.OK);
	}

	//	@PostMapping("/event/verifyEvents")
//	public ResponseEntity<String> verifyEvents(@RequestBody Events event)
//	{
//		String alpha=adminService.verifyEventDetails(event);
//		if(alpha.equals("Done")){
//			return new ResponseEntity<>(alpha,HttpStatus.OK);
//		}
//		return new ResponseEntity<>(alpha,HttpStatus.CONFLICT);
//	}
	@PostMapping("/verifyOrphanageDetails/{orpId}/{status}")
	public ResponseEntity<OrphanageDetails> verifyOrphanageDetails(@PathVariable("orpId") String  orpId, @PathVariable("status") String verificationStatus)
	{
		OrphanageDetails alpha=adminService.verifyOrphanageDetails(orpId,verificationStatus);
		if(alpha!=null){
			return new ResponseEntity<>(alpha,HttpStatus.OK);
		}
		return new ResponseEntity<>(null,HttpStatus.CONFLICT);
	}

	@PostMapping("/verifyEventDetails/{eventId}/{status}")
	public ResponseEntity<Events> verifyEventDetails(@PathVariable("eventId") String eventId, @PathVariable("status") String verificationStatus)
	{
		Events alpha = adminService.verifyEventDetails(eventId,verificationStatus);
		if(alpha!=null) {
			return new ResponseEntity<>(alpha,HttpStatus.OK);
		}
		return new ResponseEntity<>(null,HttpStatus.CONFLICT);

	}

	@GetMapping("/event/interestedPerson/{eventId}")
	public ResponseEntity<List<InterestedPerson>> getInterestedPersonByEventId(@PathVariable String eventId){
		return new ResponseEntity<>(adminService.getInterestedPersonByEventId(eventId),HttpStatus.OK);
	}

	@GetMapping("/donationList")
	public ResponseEntity<List<Donations>> getAllDonations() {
		return new ResponseEntity<>(adminService.getAllDonations(),HttpStatus.OK);
	}

	@GetMapping("/donation/orphanageId/{id}")
	public ResponseEntity<List<Donations>> getDonationsByOrphanageId(@PathVariable String id){
		return new ResponseEntity<>(adminService.getDonationsByOrphanageId(id),HttpStatus.OK);
	}

	@GetMapping("/donation/donorId/{id}")
	public ResponseEntity<List<Donations>> getDonationsByDonorId(@PathVariable String id){
		return new ResponseEntity<>(adminService.getDonationsByDonorId(id),HttpStatus.OK);
	}

	@GetMapping("/donation/{id}")
	public ResponseEntity<Donations> getDonationById(@PathVariable String id){
		Optional<Donations> donations= Optional.ofNullable(adminService.getDonationById(id));
		if(donations.isPresent()){
			return new ResponseEntity<>(donations.get(),HttpStatus.OK);
		}
		return new ResponseEntity<>(null,HttpStatus.CONFLICT);
	}
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody Admin user) {
		String alpha=adminService.registerUser(user);
		if(alpha.equals("Success")){
			return new ResponseEntity<>("Registration Successful",HttpStatus.OK);
		}
		else if(alpha.equals("Enter Correct Passcode")){
			return new ResponseEntity<>("Enter Correct Passcode",HttpStatus.CONFLICT);
		}
		else{
			return new ResponseEntity<>("You are an existing user. Please SignIn",HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody Map<String, String> loginData) {

		if (adminService.loginUser(loginData.get("email"), loginData.get("password"))) {
			return new ResponseEntity<>("Login is Successful",HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Login Failed",HttpStatus.CONFLICT);
		}
	}
	private static final List<String> ALLOWED_IMAGE_CONTENT_TYPES = Arrays.asList(
			"image/jpeg", "image/png", "image/gif", "image/bmp"
			// Add more image types if needed
	);
	@PostMapping("addPhoto/{adminId}")
	public ResponseEntity<String> addProfilePhoto(
			@PathVariable String adminId,
			@RequestParam("file")MultipartFile file) throws IOException {
		try {
			// Check if the uploaded file is an image
			if (!isImage(file)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only image files are allowed");
			}
			adminService.addProfilePhoto(adminId, file);
			return ResponseEntity.status(HttpStatus.CREATED).body("Profile photo added successfully");
		}
		catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding profile photo");
		}
	}
	private boolean isImage(MultipartFile file) {
		return file != null && ALLOWED_IMAGE_CONTENT_TYPES.contains(file.getContentType());
	}
	@GetMapping("getPhoto/{adminId}")
	public ResponseEntity<byte[]> getProfilePhoto(@PathVariable String adminId) {
		String photoBase64 = adminService.getProfilePhoto(adminId);

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
	@PutMapping("updatePhoto/{adminId}")
	public ResponseEntity<String> updateProfilePhoto(
			@PathVariable String adminId,
			@RequestParam("file") MultipartFile file) {
		try {
			if (!isImage(file)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only image files are allowed");
			}
			adminService.updateProfilePhoto(adminId, file);
			return ResponseEntity.status(HttpStatus.CREATED).body("Profile photo updated successfully");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating profile photo");
		}
	}
	@PostMapping("/sendOtp")
	public ResponseEntity<String> sendOtp(@RequestBody Admin admin) {
		String alpha=adminService.sendOtp(admin);
		if(alpha!=null){
			return new ResponseEntity<>(alpha,HttpStatus.OK);
		}
		return new ResponseEntity<>("Try Again",HttpStatus.CONFLICT);
	}

	@PostMapping("/ForgetPassword/{email}/{otp}/{create}/{confirm}")
	public ResponseEntity<String> forgetPassword(@PathVariable("email") String email,@PathVariable("create") String create,@PathVariable("otp") String otp,@PathVariable("confirm") String confirm) {
		System.out.println(create + "   " + confirm + "  " + otp);
		String alpha=adminService.forgetPassword(email,otp,create,confirm);
		if(alpha.equals("Password Changed Successfully")){
			return new ResponseEntity<>("Password Changed Successfully",HttpStatus.OK);
		}
		return new ResponseEntity<>(alpha,HttpStatus.CONFLICT);
	}

	@PutMapping("/{adminId}/editProfile")
	public ResponseEntity<Admin> editProfile(@PathVariable("adminId") String adminId,@RequestBody Admin admin){
		Admin alpha=adminService.editProfile(adminId,admin);
		if(alpha!=null)
		{
			return new ResponseEntity<>(alpha,HttpStatus.OK);
		}
		return new ResponseEntity<>(null,HttpStatus.CONFLICT);
	}

	@PostMapping("/ChangePassword/{email}/{oldPassword}/{newPassword}/{confirmNewPassword}")
	public ResponseEntity<Admin> changeAdminPassword(@PathVariable("email") String email,@PathVariable("oldPassword") String oldPassword,@PathVariable("newPassword") String newPassword,@PathVariable("confirmNewPassword") String confirmNewPassword){
		Admin alpha=adminService.changeAdminPassword(email,oldPassword,newPassword,confirmNewPassword);
		if(alpha!=null){
			return new ResponseEntity<>(alpha,HttpStatus.OK);
		}
		return new ResponseEntity<>(null,HttpStatus.CONFLICT);
	}
	@GetMapping("/admin/{adminEmail}")
	public ResponseEntity<Admin> getAdminByEmail(@PathVariable String adminEmail){
		Optional<Admin> admin=adminService.getAdminByEmail(adminEmail);
		return admin.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
	}

	@GetMapping("/requirementList")
	public ResponseEntity<List<DonationRequirements>> getAllRequirements() {
		return new ResponseEntity<>(adminService.getAllRequirements(),HttpStatus.OK);
	}

	@GetMapping("/getCertificate/{orpId}")
	public ResponseEntity<byte[]> getCertificate(@PathVariable String orpId) {
		byte[] certificateBytes = adminService.getCertificate(orpId);
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
}