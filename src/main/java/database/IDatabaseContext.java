package database;

import models.Hotel;
import models.Reservation;
import models.Room;
import models.User;

import java.util.HashMap;

public interface IDatabaseContext {
    Hotel getHotel(Integer id);
    Reservation getReservation(Integer id);
    Room getRoom(Integer id);
    User getUser(Integer id);
    HashMap<Integer, Hotel> getHotels();
    HashMap<Integer, Reservation> getReservations();
    HashMap<Integer, Room> getRooms();
    HashMap<Integer, User> getUsers();
    <T> void add(T item);
    <T> void delete(T item);
    <T> void update(T item);
    Integer getNextHotelId();
    Integer getNextReservationId();
    Integer getNextRoomId();
    Integer getNextUserId();
}
