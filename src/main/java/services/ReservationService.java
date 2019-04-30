package services;

import database.IDatabaseContext;
import exceptions.ValidationException;
import models.Reservation;
import models.User;
import utils.ReservationUtils;

import java.time.DateTimeException;
import java.util.HashMap;

public class ReservationService implements IReservationService {
    private IDatabaseContext databaseContext;

    public ReservationService(IDatabaseContext databaseContext) {
        this.databaseContext = databaseContext;
    }

    private boolean reservationValidation(Reservation reservation) {
        if (reservation != null &&
                reservation.getRoom() != null &&
                reservation.getUser() != null &&
                reservation.getStartDate().isBefore(reservation.getEndDate()))
            return true;
        else return false;
    }

    @Override
    public String add(Reservation reservation) throws ValidationException {
        if (reservationValidation(reservation)) {
            if (databaseContext.getUsers().containsValue(reservation.getUser()) &&
                    databaseContext.getRooms().containsValue(reservation.getRoom())) {
                if (ReservationUtils.hasMinutesInDate(reservation)) {
                    if (!databaseContext.getReservations().isEmpty()) {
                        boolean flag = false;
                        for (Reservation r : databaseContext.getReservations().values()) {
                            if ((ReservationUtils.isContainedIn(reservation, r) ||
                                    ReservationUtils.isEqualTo(reservation, r) ||
                                    ReservationUtils.ifDatesHaveAnIntersect(reservation, r)) &&
                                    ReservationUtils.ifRoomIsInTheSameHotel(reservation, r)) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            Integer id = databaseContext.getNextReservationId();
                            reservation.setId(id);

                            String identificator = reservation.getStartDate().toString() +
                                    reservation.getEndDate().toString() +
                                    reservation.getRoom().getNumberOfRoom() +
                                    reservation.getRoom().getHotel().getName();
                            reservation.setIdentificator(identificator);

                            databaseContext.add(reservation);
                            return identificator;
                        } else throw new DateTimeException("Selected room in this date and time is reserved " +
                                "by other person!");
                    } else {
                        Integer id = databaseContext.getNextReservationId();
                        reservation.setId(id);

                        String identificator = reservation.getStartDate().toString() +
                                reservation.getEndDate().toString() +
                                reservation.getRoom().getNumberOfRoom() +
                                reservation.getRoom().getHotel().getName();
                        reservation.setIdentificator(identificator);

                        databaseContext.add(reservation);
                        return identificator;
                    }
                } else throw new DateTimeException("Start/end date has minutes!");
            } else throw new NullPointerException();
        } else throw new ValidationException("Given reservation didn't pass validation!");
    }

    @Override
    public Reservation get(int id) {
        Reservation reservation = databaseContext.getReservation(id);
        if (reservation != null) {
            return reservation;
        } else throw new NullPointerException();
    }

    @Override
    public HashMap<Integer, Reservation> get() {
        return databaseContext.getReservations();
    }

    @Override
    public void update(Reservation reservation) throws ValidationException {
        if (reservationValidation(reservation)) {
            if (databaseContext.getUsers().containsValue(reservation.getUser()) &&
                    databaseContext.getRooms().containsValue(reservation.getRoom())) {
                if (ReservationUtils.hasMinutesInDate(reservation)) {
                    if (!databaseContext.getReservations().isEmpty()) {
                        boolean flag = false;
                        for (Reservation r : databaseContext.getReservations().values()) {
                            if ((ReservationUtils.isContainedIn(reservation, r) ||
                                    ReservationUtils.isEqualTo(reservation, r) ||
                                    ReservationUtils.ifDatesHaveAnIntersect(reservation, r)) &&
                                    ReservationUtils.ifRoomIsInTheSameHotel(reservation, r)) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            databaseContext.update(reservation);
                        } else throw new DateTimeException("Selected room in this date and time is reserved " +
                                "by other person!");
                    } else throw new NullPointerException();
                } else throw new DateTimeException("Start/end date has minutes!");
            } else throw new NullPointerException();
        } else throw new ValidationException("Given reservation didn't pass validation!");
    }

    @Override
    public void delete(int id) {
        Reservation reservation = databaseContext.getReservation(id);
        if (reservation != null) {
            databaseContext.delete(reservation);
        } else throw new NullPointerException();
    }

    @Override
    public HashMap<Integer, Reservation> getReservationsOfUser(User user) {
        if (user != null) {
            HashMap<Integer, Reservation> reservationsOfUser = new HashMap<>();
            int counter = 1;
            for (Reservation r : databaseContext.getReservations().values())
                if (r.getUser() == user) {
                    reservationsOfUser.put(counter, r);
                    counter++;
                }
            return reservationsOfUser;
        } else throw new NullPointerException();
    }
}