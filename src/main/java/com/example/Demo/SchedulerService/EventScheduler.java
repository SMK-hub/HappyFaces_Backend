package com.example.Demo.SchedulerService;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.time.format.DateTimeFormatter;


import com.example.Demo.Enum.EnumClass;
import com.example.Demo.Model.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import com.example.Demo.AdminServices.AdminService;

public class EventScheduler {

    @Autowired
    private AdminService adminService;

    @Scheduled(cron = "0 0 0 * * * *")
    public void checkEventStatus() throws ParseException {
        List<Events> events=adminService.getAllEvents();
        Iterator<Events> iterator = events.iterator();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        while (iterator.hasNext()) {
            Events event = iterator.next();

            switch (event.getEventStatus()) {
                case CANCELLED:
                    iterator.remove();
                    break;
                case PLANNED:
                    LocalDate currentDate = LocalDate.now();
                    LocalDate eventDate = LocalDate.parse(event.getDate(),formatter);
                    if (eventDate.isEqual(currentDate)) {
                        event.setEventStatus(event.setEventStatus(EnumClass.EventStatus.ONGOING));
                    } else if (eventDate.isBefore(currentDate)) {
                        event.setEventStatus(EnumClass.EventStatus.COMPLETED);
                    }
                    break;
            }
        }
    }
}
