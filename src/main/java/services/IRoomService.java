package services;

import exceptions.ElementNotFoundException;
import exceptions.ValidationException;
import models.Hotel;
import models.Room;

import java.util.HashMap;

public interface IRoomService {
    void add(Room room) throws ValidationException, ElementNotFoundException;
    Room get(int id) throws ElementNotFoundException;
    HashMap<Integer, Room> get();
    void update(Room room) throws ValidationException, ElementNotFoundException;
    void delete(int id) throws ElementNotFoundException;

    HashMap<Integer, Room> getFreeRooms();
    HashMap<Integer, Room> getFreeRooms(Hotel hotel);
}
