package com.example.Demo.Model;

import java.util.ArrayList;
import java.util.List;

import com.example.Demo.Enum.EnumClass;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("event")
public class Events {
	@Id
	private String id;
	private String orpId;
    private String title;
    private String description;
    private String date;
    private String time;
    private EnumClass.EventStatus eventStatus;
    private EnumClass.VerificationStatus verificationStatus;
	
	public Events(String id, String orpId, String title, String description, String date, String time,
			EnumClass.VerificationStatus verificationStatus, List<InterestedPerson> interestedPersons) {
		super();
		this.id = id;
		this.orpId = orpId;
		this.title = title;
		this.description = description;
		this.date = date;
		this.time = time;
		this.verificationStatus = verificationStatus;
	}

	public String getOrpId() {
		return orpId;
	}

	public void setOrpId(String orpId) {
		this.orpId = orpId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



    public EnumClass.EventStatus getEventStatus() {
        return eventStatus;
    }

    public EnumClass.EventStatus setEventStatus(EnumClass.EventStatus eventStatus) {
        this.eventStatus = eventStatus;
        return eventStatus;
    }

	public Events() {
    }

    public void Event(String title, String description, String date, String time, EnumClass.VerificationStatus verificationStatus) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.verificationStatus = verificationStatus;

    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public EnumClass.VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(EnumClass.VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }


    // toString() method
    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", verificationStatus=" + verificationStatus +
                '}';
    }


}
