package com.example.Demo.Model;

import com.example.Demo.Enum.EnumClass;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;

@Document(collection = "admin")
public class Admin {
    
	@Id
    private String adminId;
    private String name;
    private String email;
	private byte[] profilePhoto;
    private String password;
    private EnumClass.Roles Role;
	private String passcode;
	private String contact;
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAdminId() {
		return adminId;
	}

	@Override
	public String toString() {
		return "Admin{" +
				"adminId='" + adminId + '\'' +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", profilePhoto=" + Arrays.toString(profilePhoto) +
				", password='" + password + '\'' +
				", Role='" + Role + '\'' +
				", passcode='" + passcode + '\'' +
				", contact='" + contact + '\'' +
				'}';
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public Admin() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public EnumClass.Roles getRole() {
		return Role;
	}

	public void setRole(EnumClass.Roles Role) {
		this.Role = Role;
	}

	public String getPasscode() {
		return passcode;
	}

	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}
	public byte[] getProfilePhoto() {
		return profilePhoto;
	}
	public void setProfilePhoto(byte[] profilePhoto) {
		this.profilePhoto = profilePhoto;
	}
}