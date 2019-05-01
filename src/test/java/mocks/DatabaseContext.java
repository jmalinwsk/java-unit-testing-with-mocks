package mocks;

import database.IDatabaseContext;
import exceptions.ObjectNotFoundException;
import models.Hotel;
import models.Reservation;
import models.Room;
import models.User;

import java.util.HashMap;

public class DatabaseContext implements IDatabaseContext {
    private HashMap<Integer, Hotel> hotels = new HashMap<>();
    private HashMap<Integer, Room> rooms = new HashMap<>();
    private HashMap<Integer, Reservation> reservations = new HashMap<>();
    private HashMap<Integer, User> users = new HashMap<>();
    private Integer nextHotelId = 0;
    private Integer nextReservationId = 0;
    private Integer nextRoomId = 0;
    private Integer nextUserId = 0;

    @Override
    public Hotel getHotel(Integer id) {
        return hotels.get(id);
    }

    @Override
    public Reservation getReservation(Integer id) {
        return reservations.get(id);
    }

    @Override
    public Room getRoom(Integer id) {
        return rooms.get(id);
    }

    @Override
    public User getUser(Integer id) {
        return users.get(id);
    }

    @Override
    public HashMap<Integer, Hotel> getHotels() {
        return hotels;
    }

    @Override
    public HashMap<Integer, Reservation> getReservations() {
        return reservations;
    }

    @Override
    public HashMap<Integer, Room> getRooms() {
        return rooms;
    }

    @Override
    public HashMap<Integer, User> getUsers() {
        return users;
    }

    @Override
    public <T> void add(T item) {
        if(item instanceof Hotel)
            hotels.put(nextHotelId, (Hotel)item);
        else if (item instanceof Reservation)
            reservations.put(nextReservationId, (Reservation)item);
        else if (item instanceof Room)
            rooms.put(nextRoomId, (Room)item);
        else if (item instanceof User)
            users.put(nextUserId, (User)item);
        else try {
            throw new ObjectNotFoundException();
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> void delete(T item) {
        if(item instanceof Hotel)
            hotels.remove(((Hotel) item).getId());
        else if (item instanceof Reservation)
            hotels.remove(((Reservation) item).getId());
        else if (item instanceof Room)
            hotels.remove(((Room) item).getId());
        else if (item instanceof User)
            hotels.remove(((User) item).getId());
        else try {
                throw new ObjectNotFoundException();
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }
    }

    @Override
    public <T> void update(T item) {
        if(item instanceof Hotel) {
            hotels.replace(((Hotel)item).getId(), (Hotel)item);
        } else if (item instanceof Reservation) {
            reservations.replace(((Reservation)item).getId(), (Reservation)item);
        } else if (item instanceof Room) {
            rooms.replace(((Room)item).getId(), (Room) item);
        } else if (item instanceof User) {
            users.replace(((User)item).getId(), (User) item);
        } else try {
            throw new ObjectNotFoundException();
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer getNextHotelId() {
        nextHotelId++;
        return nextHotelId;
    }

    @Override
    public Integer getNextReservationId() {
        nextReservationId++;
        return nextReservationId;
    }

    @Override
    public Integer getNextRoomId() {
        nextRoomId++;
        return nextRoomId;
    }

    @Override
    public Integer getNextUserId() {
        nextUserId++;
        return nextUserId;
    }
}
