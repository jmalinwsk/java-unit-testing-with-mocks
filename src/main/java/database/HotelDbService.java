package database;

import models.Hotel;

import java.sql.SQLException;

public interface HotelDbService {
    void add(Hotel hotel);
    Hotel get(int id);
    void update(Hotel hotel);
    void delete(int id);
}
