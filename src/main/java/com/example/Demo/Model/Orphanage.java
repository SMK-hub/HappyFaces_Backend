package com.example.Demo.Model;


import com.example.Demo.Enum.EnumClass;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orphanage")
public class Orphanage {

    @Id
    private String orpId;
    private String name;
    private String email;
    private byte[] profilePhoto;
    private String password;
    private String contact;
    private EnumClass.Roles Role;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getOrpId() {
        return orpId;
    }

    public void setOrpId(String orpId) {
        this.orpId = orpId;
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

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Orphanage(String id, String name, String email, String password, String contact, EnumClass.Roles Role) {
        super();
        this.orpId = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.contact = contact;
        this.Role = Role;
    }

    public Orphanage(String contact) {
        super();
        this.contact = contact;
    }
    public Orphanage(){
        super();
    }
}

