package services;

import exceptions.ValidationException;
import models.Reservation;
import models.User;

import java.util.HashMap;

public interface IReservationService {
    String add(Reservation reservation) throws ValidationException;
    Reservation get(int id);
    HashMap<Integer, Reservation> get();
    void update(Reservation reservation) throws ValidationException;
    void delete(int id);
    HashMap<Integer, Reservation> getReservationsOfUser(User user);
}
