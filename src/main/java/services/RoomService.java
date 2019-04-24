package services;

import models.Room;

public interface RoomService {
    void add(Room room);
    Room get(int id);
    void update(Room room);
    void delete(int id);
}
