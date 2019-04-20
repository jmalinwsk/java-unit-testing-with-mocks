package models;

public class Room {
    private Integer id;
    private Integer numberOfRoom;
    private Hotel hotel;
    private Integer amountOfPeople;

    public Room() {}

    public Room(Hotel hotel, Integer numberOfRoom, Integer amountOfPeople) {
        this.hotel = hotel;
        this.numberOfRoom = numberOfRoom;
        this.amountOfPeople = amountOfPeople;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumberOfRoom() {
        return numberOfRoom;
    }

    public void setNumberOfRoom(Integer numberOfRoom) {
        this.numberOfRoom = numberOfRoom;
    }

    public Integer getAmountOfPeople() {
        return amountOfPeople;
    }

    public void setAmountOfPeople(Integer amountOfPeople) {
        this.amountOfPeople = amountOfPeople;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
