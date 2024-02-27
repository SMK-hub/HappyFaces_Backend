package com.example.Demo.Model;

import com.example.Demo.Enum.EnumClass;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "donor")

public class Donor {

    @Id
    private String donorId;
    private String name;
    private String email;
    private byte[] profilePhoto;
    private String password;
    private EnumClass.Roles Role;
    private String contact;
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
    public Donor(String id, String name, String email, String password, EnumClass.Roles Role,String contact) {
        super();
        this.donorId = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.Role = Role;
        this.contact = contact;
    }

    public Donor() {
        super();
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    @Override
    public String toString() {
        return "Donor [id=" + donorId + ", name=" + name + ", email=" + email + ", password=" + password + ", Role=" + Role
                + "]";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

}
