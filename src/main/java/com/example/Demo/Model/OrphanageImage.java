package com.example.Demo.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;

@Document(collection = "orphanage_images")
public class OrphanageImage {

    @Id
    private String id;
    private String orphanageId;
    private byte[] image;

    @Override
    public String toString() {
        return "OrphanageImage{" +
                "id='" + id + '\'' +
                ", orphanageId='" + orphanageId + '\'' +
                ", image=" + Arrays.toString(image) +
                '}';
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrphanageId() {
        return orphanageId;
    }

    public void setOrphanageId(String orphanageId) {
        this.orphanageId = orphanageId;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }



}
