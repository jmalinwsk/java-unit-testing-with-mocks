package services;

import models.Hotel;

import java.sql.SQLException;

public interface IHotelService {
    void add(Hotel hotel);
    Hotel get(int id);
    void update(Hotel hotel);
    void delete(int id);
}
