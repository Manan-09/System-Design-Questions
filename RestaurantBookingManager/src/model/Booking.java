package model;

import helper.IDGenerator;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class Booking {
    private String id;
    private String restaurant;
    private String user;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;

    public Booking(String id, String restaurant, String user, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.restaurant = restaurant;
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public String getUser() {
        return user;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Booking{" + "id='" + id + '\'' + ", restaurant='" + restaurant + '\'' + ", user='" + user + '\'' +
               ", startTime=" + startTime + ", endTime=" + endTime + ", createdAt=" + createdAt + '}';
    }
}
