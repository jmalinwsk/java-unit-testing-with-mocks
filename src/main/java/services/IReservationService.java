package services;

import models.Reservation;
import models.User;

import java.util.HashMap;
import java.util.List;

public interface IReservationService {
    void add(Reservation reservation);
    Reservation get(int id);
    HashMap<Integer, Reservation> get();
    void update(Reservation reservation);
    void delete(int id);
    HashMap<Integer, Reservation> getReservationsOfUser(User user);
}
