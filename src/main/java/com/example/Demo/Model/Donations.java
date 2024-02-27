package com.example.Demo.Model;

import com.example.Demo.Enum.EnumClass;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Stack;

@Document(collection = "donationDetails")
public class Donations {

    @Id
    private String donid;
    private String donorId;
    private String orpId;
    private String orphanageName;
    private String amount;
    private EnumClass.Status status;
    private String dateTime;
    private String transactionId;


    public String getDonorId() {
        return donorId;
    }

    public String getOrpId() {
        return orpId;
    }

    public String getOrphanageName() {
        return orphanageName;
    }

    public void setOrphanageName(String orphanageName) {
        this.orphanageName = orphanageName;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }



    public String getDonid() {
        return donid;
    }

    public void setDonid(String donid) {
        this.donid = donid;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public void setOrpId(String orpId) {
        this.orpId = orpId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public Donations() {
        // Default constructor
        super();
    }

    public Donations(String donorId, String orpId, String amount, EnumClass.Status status, String dateTime, String transactionId,String orphanageName) {
        this.donorId = donorId;
        this.orpId = orpId;
        this.amount = amount;
        this.status = status;
        this.dateTime = dateTime;
        this.transactionId = transactionId;
        this.orphanageName=orphanageName;
    }

    // Getters and setters

    public String getId() {
        return donid;
    }

    public void setId(String donid) {
        this.donid = donid;
    }
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public EnumClass.Status getStatus() {
        return status;
    }

    public void setStatus(EnumClass.Status status) {
        this.status = status;
    }

    public String getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "Donation{" +
                "donid='" + donid + '\'' +
                ", donorId=" + donorId +
                ", orpId=" + orpId +
                ", amount='" + amount + '\'' +
                ", status='" + status + '\'' +
                ", date='" + dateTime + '\'' +
                '}';
    }

}
