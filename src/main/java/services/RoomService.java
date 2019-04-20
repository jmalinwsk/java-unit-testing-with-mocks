package services;

import database.Database;
import models.Room;

public class RoomService {
    /** Validation of room.
     * @return true if validation is valid or false if validation is invalid
     */
    public boolean roomValidation(Room room) {
        if(room != null &&
            room.getNumberOfRoom() > 0 &&
            room.getAmountOfPeople() > 0)
            return true;
        else return false;
    }

    /** Validates room value + checking if hotel associated to room exists
     * in database exists and if valid, adds room to the database.
     * @throws IllegalArgumentException when validation of room is wrong
     * @throws NullPointerException when hotel associated to room doesn't
     * exists in database
     */
    public void addRoomToDatabase(Database database, Room newRoom) {
        if(roomValidation(newRoom)) {
            if(database != null &&
                    database.getHotels().containsValue(newRoom.getHotel())) {
                Integer id = database.getNextRoomId();
                newRoom.setId(id);
                    database.getRooms().put(id, newRoom);
            } else throw new NullPointerException();
        } else throw new IllegalArgumentException();
    }
}
