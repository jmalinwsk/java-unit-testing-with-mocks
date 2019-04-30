package services;

import database.IDatabaseContext;
import exceptions.ValidationException;
import models.Room;

import java.util.HashMap;

public class RoomService implements IRoomService {
    private IDatabaseContext databaseContext;

    public RoomService(IDatabaseContext databaseContext) {
        this.databaseContext = databaseContext;
    }

    private boolean roomValidation(Room room) {
        if(room != null &&
                room.getNumberOfRoom() > 0 &&
                room.getAmountOfPeople() > 0)
            return true;
        else return false;
    }

    @Override
    public void add(Room room) throws ValidationException {
        if(databaseContext.getHotels().containsValue(room.getHotel())) {
            if (roomValidation(room)) {
                Integer id = databaseContext.getNextUserId();
                room.setId(id);

                databaseContext.add(room);
            } else throw new ValidationException(
                    "Given room didn't pass validation!");
        } else throw new NullPointerException();
    }

    @Override
    public Room get(int id) {
        Room room = databaseContext.getRoom(id);
        if(room != null) {
            return room;
        } else throw new NullPointerException();
    }

    @Override
    public HashMap<Integer, Room> get() {
        return databaseContext.getRooms();
    }

    @Override
    public void update(Room room) throws ValidationException {
        if(roomValidation(room)) {
            databaseContext.update(room);
        }
        else throw new ValidationException(
                "Given room didn't pass validation!");
    }

    @Override
    public void delete(int id) {
        Room room = databaseContext.getRoom(id);
        if(room != null) {
            databaseContext.delete(room);
        } else throw new NullPointerException();
    }

    @Override
    public HashMap<Integer, Room> getFreeRooms() {
        return null;
    }
}
