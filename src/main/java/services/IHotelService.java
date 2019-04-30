package services;

import exceptions.ValidationException;
import models.Hotel;

import java.sql.SQLException;
import java.util.HashMap;

public interface IHotelService {
    void add(Hotel hotel) throws ValidationException;
    Hotel get(int id);
    HashMap<Integer, Hotel> get();
    void update(Hotel hotel) throws ValidationException;
    void delete(int id);
}
