package services;

import models.Reservation;
import models.User;

import java.util.List;

public interface IReservationService {
    void add(Reservation reservation);
    Reservation get(int id);
    void update(Reservation reservation);
    void delete(int id);
    List<Reservation> getReservationsOfUser(User user);
}
