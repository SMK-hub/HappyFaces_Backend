package com.example.Demo.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "donationRequirements")
public class DonationRequirements {

    @Id
    private String id;
    private String orpId;
    private String donorId;
    private String description;
    @Override
    public String toString() {
        return "DonationRequirements{" +
                "id='" + id + '\'' +
                ", orpId='" + orpId + '\'' +
                ", donorId='" + donorId + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrpId() {
        return orpId;
    }

    public void setOrpId(String orpId) {
        this.orpId = orpId;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DonationRequirements(String id, String orpId, String donorId, String description) {
        this.id = id;
        this.orpId = orpId;
        this.donorId = donorId;
        this.description = description;
    }

    public DonationRequirements() {
        super();
    }
}
