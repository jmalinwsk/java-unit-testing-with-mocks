package services;

import database.IDatabaseContext;
import exceptions.ValidationException;
import models.Hotel;

import java.util.HashMap;

public class HotelService implements IHotelService {
    private IDatabaseContext databaseContext;

    public HotelService(IDatabaseContext databaseContext) {
        this.databaseContext = databaseContext;
    }

    private boolean hotelValidation(Hotel hotel) {
        if(hotel != null &&
                hotel.getName() != null &&
                hotel.getOpenHour() != null &&
                hotel.getCloseHour() != null &&
                !hotel.getName().equals("") &&
                hotel.getOpenHour().isBefore(hotel.getCloseHour()))
            return true;
        else return false;
    }

    @Override
    public void add(Hotel hotel) throws ValidationException {
        if(hotelValidation(hotel)) {
            Integer id = databaseContext.getNextHotelId();
            hotel.setId(id);

            databaseContext.add(hotel);
        }
        else throw new ValidationException(
                "Given hotel didn't pass validation!");
    }

    @Override
    public Hotel get(int id) {
        Hotel hotel = databaseContext.getHotel(id);
        if(hotel != null) {
            return hotel;
        } else throw new NullPointerException();
    }

    @Override
    public HashMap<Integer, Hotel> get() {
        return databaseContext.getHotels();
    }

    @Override
    public void update(Hotel hotel) throws ValidationException {
        if(hotelValidation(hotel)) {
            databaseContext.update(hotel);
        }
        else throw new ValidationException(
                "Given hotel didn't pass validation!");
    }

    @Override
    public void delete(int id) {
        Hotel hotel = databaseContext.getHotel(id);
        if(hotel != null) {
            databaseContext.delete(hotel);
        } else throw new NullPointerException();
    }
}
