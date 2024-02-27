package com.example.Demo.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("interestedPerson")
public class InterestedPerson {
    @Id
    private String id;
    private String donorId;
    private String eventId;
    private String name;
    private String email;
    private String contact;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public InterestedPerson() {
        super();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public InterestedPerson(String contact) {
        super();
    }

    public InterestedPerson(String donorId, String name, String email, String contact,String eventId) {
        this.donorId = donorId;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.eventId = eventId;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    // toString() method
    @Override
    public String toString() {
        return "InterestedPerson{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' + "DonorId=" + donorId +
                '}';
    }
}
