package services;

import exceptions.ElementNotFoundException;
import exceptions.ValidationException;
import models.Reservation;
import models.User;

import java.util.HashMap;

public interface IReservationService {
    String add(Reservation reservation) throws ValidationException, ElementNotFoundException;
    Reservation get(int id) throws ElementNotFoundException;
    HashMap<Integer, Reservation> get();
    void update(Reservation reservation) throws ValidationException, ElementNotFoundException;
    void delete(int id) throws ElementNotFoundException;

    HashMap<Integer, Reservation> getReservationsOfUser(User user);
}
