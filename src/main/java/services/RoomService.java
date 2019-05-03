package services;

import database.IDatabaseContext;
import exceptions.ElementNotFoundException;
import exceptions.ValidationException;
import models.Hotel;
import models.Reservation;
import models.Room;

import java.util.HashMap;

public class RoomService implements IRoomService {
    private IDatabaseContext databaseContext;

    public RoomService(IDatabaseContext databaseContext) {
        this.databaseContext = databaseContext;
    }

    public boolean roomValidation(Room room) {
        return room != null &&
                room.getNumberOfRoom() > 0 &&
                room.getAmountOfPeople() > 0 &&
                room.getHotel() != null;
    }

    @Override
    public void add(Room room) throws ValidationException, ElementNotFoundException {
        if (roomValidation(room)) {
            if(databaseContext.getHotels().containsValue(room.getHotel())) {
                Integer id = databaseContext.getNextUserId();
                room.setId(id);

                databaseContext.add(room);
            } else throw new ElementNotFoundException(
                    "Room with id" + room.getId()  + " is not found.");
        } else throw new ValidationException(
                "Given room didn't pass validation!");
    }

    @Override
    public Room get(int id) throws ElementNotFoundException {
        Room room = databaseContext.getRoom(id);
        if(room != null) {
            return room;
        } else throw new ElementNotFoundException(
                "Room with id" + id  + " is not found.");
    }

    @Override
    public HashMap<Integer, Room> get() {
        return databaseContext.getRooms();
    }

    @Override
    public void update(Room room) throws ValidationException, ElementNotFoundException {
        if(roomValidation(room)) {
            Room checkIfRoomExists = databaseContext.getRoom(room.getId());
            if(checkIfRoomExists != null) {
                databaseContext.update(room);
            } else throw new ElementNotFoundException(
                    "Room with id " + room.getId() + " is not found.");
        }
        else throw new ValidationException(
                "Given room didn't pass validation!");
    }

    @Override
    public void delete(int id) throws ElementNotFoundException {
        Room room = databaseContext.getRoom(id);
        if(room != null) {
            databaseContext.delete(room);
        } else throw new ElementNotFoundException(
                "Room with id " + id  + " is not found.");
    }

    @Override
    public HashMap<Integer, Room> getFreeRooms() {
        HashMap<Integer, Room> freeRooms = databaseContext.getRooms();

        for(Reservation reservation : databaseContext.getReservations().values())
            for(Room room : freeRooms.values())
                if(reservation.getRoom() == room)
                    freeRooms.remove(room.getId(), room);

        return freeRooms;
    }

    @Override
    public HashMap<Integer, Room> getFreeRooms(Hotel hotel) {
        HashMap<Integer, Room> freeRooms = databaseContext.getRooms();

        for(Reservation reservation : databaseContext.getReservations().values())
            for(Room room : freeRooms.values())
                if(reservation.getRoom() == room &&
                        !reservation.getRoom().getHotel().getId().equals(hotel.getId()))
                    freeRooms.remove(room.getId(), room);

        return freeRooms;
    }
}
