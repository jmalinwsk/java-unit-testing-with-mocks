package database;

import models.Hotel;
import models.Reservation;
import models.Room;
import models.User;

import java.util.HashMap;

public interface IDatabaseContext {
    HashMap<Integer, Hotel> getHotels();
    HashMap<Integer, Reservation> getReservations();
    HashMap<Integer, Room> getRooms();
    HashMap<Integer, User> getUsers();
    <T> void add(T item);
    <T> void delete(T item);
    <T> void update(T item);
    <T> T get(T item);
}
