package models;

import org.joda.time.DateTime;

public class Reservation {
    private Integer id;
    private DateTime startDate;
    private DateTime endDate;
    private Room room;
    private User user;
    private String identifier;

    public Reservation() {}

    public Reservation(Integer id, DateTime startDate, DateTime endDate, User user, Room room) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.room = room;
    }

    public Reservation(DateTime startDate, DateTime endDate, User user, Room room) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.room = room;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
