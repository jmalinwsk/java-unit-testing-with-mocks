package models;

public class Room {
    private Integer id;
    private Integer roomNumber;
    private Hotel hotel;
    private Integer numberOfGuests;

    public Room() {}

    public Room(Integer id, Hotel hotel, Integer roomNumber, Integer numberOfGuests) {
        this.id = id;
        this.hotel = hotel;
        this.roomNumber = roomNumber;
        this.numberOfGuests = numberOfGuests;
    }

    public Room(Hotel hotel, Integer roomNumber, Integer numberOfGuests) {
        this.hotel = hotel;
        this.roomNumber = roomNumber;
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
