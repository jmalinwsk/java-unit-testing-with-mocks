package database;

import models.Room;

public interface RoomDbService {
    void add(Room room);
    Room get(int id);
    void update(Room room);
    void delete(int id);
}
