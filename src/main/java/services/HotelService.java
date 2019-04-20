package services;

import database.Database;
import models.Hotel;

import java.time.DateTimeException;

public class HotelService {
    /** Validation of hotel.
     * @return true if hotel is valid or false if hotel is invalid
     */
    public boolean hotelValidation(Hotel hotel) {
        if(hotel != null &&
                hotel.getName() != null &&
                hotel.getOpenHour() != null &&
                hotel.getCloseHour() != null &&
                !hotel.getName().equals("") &&
                hotel.getOpenHour().isBefore(hotel.getCloseHour()))
            return true;
        else return false;
    }

    /** Validates hotel value and if valid, adds hotel to the database.
     * @throws IllegalArgumentException when validation of hotel is wrong
     */
    public void addHotelToDatabase(Database database, Hotel newHotel) {
        if (hotelValidation(newHotel)) {
            if (database != null) {
                Integer id = database.getNextHotelId();
                newHotel.setId(id);

                database.getHotels().put(id, newHotel);
            } else throw new NullPointerException();
        } else throw new IllegalArgumentException();
    }
}
