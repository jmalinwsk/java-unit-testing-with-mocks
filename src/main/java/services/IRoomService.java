package services;

import exceptions.ValidationException;
import models.Room;

import java.util.HashMap;
import java.util.List;

public interface IRoomService {
    void add(Room room) throws ValidationException;
    Room get(int id);
    HashMap<Integer, Room> get();
    void update(Room room) throws ValidationException;
    void delete(int id);
    HashMap<Integer, Room> getFreeRooms();
}
