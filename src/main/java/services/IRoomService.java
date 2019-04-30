package services;

import models.Room;

import java.util.HashMap;
import java.util.List;

public interface IRoomService {
    void add(Room room);
    Room get(int id);
    HashMap<Integer, Room> get();
    void update(Room room);
    void delete(int id);
    HashMap<Integer, Room> getFreeRooms();
}
