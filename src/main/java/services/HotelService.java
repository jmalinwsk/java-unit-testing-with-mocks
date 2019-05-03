package services;

import database.IDatabaseContext;
import exceptions.ElementNotFoundException;
import exceptions.ValidationException;
import models.Hotel;
import models.Reservation;
import org.joda.time.LocalTime;

import java.time.DateTimeException;
import java.util.HashMap;

public class HotelService implements IHotelService {
    private IDatabaseContext databaseContext;

    public HotelService(IDatabaseContext databaseContext) {
        this.databaseContext = databaseContext;
    }

    public boolean hotelValidation(Hotel hotel) {
        return hotel != null &&
                hotel.getName() != null &&
                hotel.getOpenHour() != null &&
                hotel.getCloseHour() != null &&
                !hotel.getName().equals("") &&
                hotel.getOpenHour().isBefore(hotel.getCloseHour());
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
    public Hotel get(int id) throws ElementNotFoundException {
        Hotel hotel = databaseContext.getHotel(id);
        if(hotel != null) {
            return hotel;
        } else throw new ElementNotFoundException("Hotel with id " + id + " is not found.");
    }

    @Override
    public HashMap<Integer, Hotel> get() {
        return databaseContext.getHotels();
    }

    @Override
    public void update(Hotel hotel) throws ValidationException, ElementNotFoundException {
        if(hotelValidation(hotel)) {
            Hotel checkIfHotelExist = databaseContext.getHotel(hotel.getId());
            if(checkIfHotelExist != null) {
                databaseContext.update(hotel);
            } else throw new ElementNotFoundException(hotel.getName() + " is not found.");
        } else throw new ValidationException("Given hotel didn't pass validation!");
    }

    @Override
    public void delete(int id) throws ElementNotFoundException {
        Hotel hotel = databaseContext.getHotel(id);
        if(hotel != null) {
            databaseContext.delete(hotel);
        } else throw new ElementNotFoundException(id + " is not found.");
    }

    @Override
    public HashMap<Integer, Hotel> getHotelsWithFreeRooms() {
        HashMap<Integer, Hotel> hotelsWithFreeRooms = databaseContext.getHotels();

        for(Reservation reservation : databaseContext.getReservations().values())
            for(Hotel hotel : hotelsWithFreeRooms.values())
                if(reservation.getRoom().getHotel().equals(hotel))
                    hotelsWithFreeRooms.remove(hotel.getId());

        return hotelsWithFreeRooms;
    }

    @Override
    public void changeOpenHours(Integer id, LocalTime openHour, LocalTime closeHour) throws ElementNotFoundException {
        if(id != null &&
            openHour != null &&
            closeHour != null) {
            if(openHour.isBefore(closeHour)) {
                if(databaseContext.getHotels().containsKey(id)) {
                    databaseContext.getHotel(id).setOpenHour(openHour);
                    databaseContext.getHotel(id).setCloseHour(closeHour);
                } else throw new ElementNotFoundException("Hotel with given id is not found!");
            } else throw new DateTimeException("Open hour is before close hour!");
        } else throw new IllegalArgumentException();
    }
}
