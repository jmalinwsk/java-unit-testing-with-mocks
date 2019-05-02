package services;

import exceptions.ElementNotFoundException;
import exceptions.ValidationException;
import models.Hotel;

import java.util.HashMap;

public interface IHotelService {
    void add(Hotel hotel) throws ValidationException;
    Hotel get(int id) throws ElementNotFoundException;
    HashMap<Integer, Hotel> get();
    void update(Hotel hotel) throws ValidationException, ElementNotFoundException;
    void delete(int id) throws ElementNotFoundException;
}
