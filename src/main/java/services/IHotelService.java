package services;

import exceptions.ElementNotFoundException;
import exceptions.ValidationException;
import models.Hotel;
import models.Room;
import org.joda.time.LocalTime;

import java.util.HashMap;

public interface IHotelService {
    void add(Hotel hotel) throws ValidationException;
    Hotel get(int id) throws ElementNotFoundException;
    HashMap<Integer, Hotel> get();
    void update(Hotel hotel) throws ValidationException, ElementNotFoundException;
    void delete(int id) throws ElementNotFoundException;

    HashMap<Integer, Hotel> getHotelsWithFreeRooms();
    void changeOpenHours(Integer id, LocalTime openHour, LocalTime closeHour) throws ElementNotFoundException;
    HashMap<Integer, Room> getRooms(Hotel hotel);
}
