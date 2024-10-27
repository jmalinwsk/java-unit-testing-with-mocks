package utils;

import models.Reservation;

public class ReservationUtils {
    public static boolean hasMinutesInDate(Reservation reservation) {
        return reservation.getStartDate().getMinuteOfDay() % 60 != 0 ||
                reservation.getEndDate().getMinuteOfDay() % 60 != 0;
    }

    public static boolean isContainedIn(Reservation r1, Reservation r2) {
        return (r1.getStartDate().isAfter(r2.getStartDate()) &&
                r1.getEndDate().isBefore(r2.getEndDate()));
    }

    public static boolean isEqualTo(Reservation r1, Reservation r2) {
        return (r1.getStartDate().isEqual(r2.getStartDate()) &&
                r1.getEndDate().isEqual(r2.getEndDate()));
    }

    public static boolean ifRoomIsInTheSameHotel(Reservation r1, Reservation r2) {
        return (r1.getRoom().getHotel().getId().equals(r2.getRoom().getHotel().getId()) &&
                        r1.getRoom().getId().equals(r2.getRoom().getId()));
    }

    public static boolean ifDatesHaveAnIntersect(Reservation r1, Reservation r2) {
        if(r1.getStartDate().isBefore(r2.getStartDate()))
            return r2.getStartDate().isBefore(r1.getEndDate());
        else return r1.getStartDate().isBefore(r2.getEndDate());
    }
}
