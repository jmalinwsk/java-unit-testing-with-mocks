package database;

import models.Reservation;

public interface ReservationDbService {
    void add(Reservation reservation);
    Reservation get(int id);
    void update(Reservation reservation);
    void delete(int id);
}
