package services;

import database.Database;
import models.Reservation;
import models.User;
import utils.ReservationUtils;

import java.time.DateTimeException;
import java.util.HashMap;

public class ReservationService {

    public boolean reservationValidation(Reservation reservation) {
        if (reservation != null &&
                reservation.getRoom() != null &&
                reservation.getUser() != null &&
                reservation.getStartDate().isBefore(reservation.getEndDate()))
            return true;
        else return false;
    }

    public String addReservationToDatabase(Database database, Reservation newReservation) {
        if(reservationValidation(newReservation)) {
            if(database != null &&
                    database.getUsers().containsValue(newReservation.getUser()) &&
                    database.getRooms().containsValue(newReservation.getRoom())) {
                if(!database.getReservations().isEmpty()) {
                    if(ReservationUtils.hasMinutesInDate(newReservation))
                        throw new DateTimeException("Start/end date has minutes!");

                    boolean flag = false;
                    for (Reservation r : database.getReservations().values()) {
                        if((ReservationUtils.isContainedIn(newReservation, r) ||
                        ReservationUtils.isEqualTo(newReservation, r) ||
                        ReservationUtils.ifDatesHaveAnIntersect(newReservation, r)) &&
                        ReservationUtils.ifRoomIsInTheSameHotel(newReservation, r)) {
                            flag = true;
                            break;
                        }
                    }
                    if(!flag) {
                        Integer id = database.getNextReservationId();
                        newReservation.setId(id);

                        String identificator = newReservation.getStartDate().toString() +
                                newReservation.getEndDate().toString() +
                                newReservation.getRoom().getNumberOfRoom() +
                                newReservation.getRoom().getHotel().getName();
                        newReservation.setIdentificator(identificator);

                    database.getReservations().put(id, newReservation);

                    return identificator;
                    } else throw new DateTimeException("Selected room in this date and time is reserved " +
                            "by other person!");
                } else {
                    if(ReservationUtils.hasMinutesInDate(newReservation))
                        throw new DateTimeException("Start/end date has minutes!");

                    Integer id = database.getNextReservationId();
                    newReservation.setId(id);

                    String identificator = newReservation.getStartDate().toString() +
                            newReservation.getEndDate().toString() +
                            newReservation.getRoom().getNumberOfRoom() +
                            newReservation.getRoom().getHotel().getName();
                    newReservation.setIdentificator(identificator);

                    database.getReservations().put(id, newReservation);
                    return identificator;
                }
            } else throw new NullPointerException();
        } else throw new IllegalArgumentException();
    }

    public HashMap<Integer, Reservation> getReservationsOfUser(Database database, User user) {
        if (user != null && database != null) {
            HashMap<Integer, Reservation> reservationsOfUser = new HashMap<>();
            int counter = 1;
            for (Reservation r : database.getReservations().values())
                if (r.getUser() == user) {
                    reservationsOfUser.put(counter, r);
                    counter++;
                }
            return reservationsOfUser;
        } else throw new NullPointerException();
    }
}
