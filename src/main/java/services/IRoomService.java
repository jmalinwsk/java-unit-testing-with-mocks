package services;

import exceptions.ElementNotFoundException;
import exceptions.ValidationException;
import models.Room;

import java.util.HashMap;
import java.util.List;

public interface IRoomService {
    void add(Room room) throws ValidationException, ElementNotFoundException;
    Room get(int id) throws ElementNotFoundException;
    HashMap<Integer, Room> get();
    void update(Room room) throws ValidationException;
    void delete(int id) throws ElementNotFoundException;
    HashMap<Integer, Room> getFreeRooms();
}
