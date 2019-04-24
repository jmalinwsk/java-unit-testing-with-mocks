package services;

import models.Reservation;

public interface ReservationService {
    void add(Reservation reservation);
    Reservation get(int id);
    void update(Reservation reservation);
    void delete(int id);
}
