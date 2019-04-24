package database;

import models.Hotel;
import models.Reservation;
import models.Room;
import models.User;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationDbServiceTest {
    @Mock
    ReservationDbService reservationDbService;

    private Hotel hotel;
    private Room room;
    private User user;
    private Reservation reservation;

    @BeforeEach
    void init() {
        reservationDbService = mock(ReservationDbService.class);
        hotel = new Hotel();
        hotel.setId(1);
        hotel.setName("Sample name");
        hotel.setOpenHour(new LocalTime(8));
        hotel.setCloseHour(new LocalTime(22));
        room = new Room();
        room.setId(1);
        room.setHotel(hotel);
        room.setAmountOfPeople(2);
        room.setNumberOfRoom(435);
        user = new User();
        user.setId(1);
        user.setEmail("sample@email.com");
        reservation = new Reservation(
                new DateTime(2019, 05, 05, 11, 0),
                new DateTime(2019, 05, 06, 11, 0),
                user, room);
        reservation.setId(1);
    }

    @Test
    void validAddTest() {
        assertDoesNotThrow(() -> reservationDbService.add(reservation));
    }

    @Test
    void addThrowsWhenReservationIsNull() {
        reservationDbService.add(null);
        doThrow(NullPointerException.class).when(reservationDbService).add(null);

        assertThrows(NullPointerException.class,
                () -> reservationDbService.add(null));
    }

    @Test
    void validGetTest() {
        when(reservationDbService.get(1)).thenReturn(reservation);

        Reservation result = reservationDbService.get(1);

        assertEquals(reservation, result);
    }

    @Test
    void getThrowsWhenIdIsZero() {
        when(reservationDbService.get(0)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> reservationDbService.get(0));
    }

    @Test
    void getThrowsWhenIdIsNegative() {
        when(reservationDbService.get(-1)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> reservationDbService.get(-1));
    }

    @Test
    void getReturnsNullWhenReservationIsNotFound() {
        when(reservationDbService.get(2)).thenReturn(null);

        assertNull(reservationDbService.get(2));
    }

    @Test
    void validUpdateTest() {
        assertDoesNotThrow(() -> reservationDbService.update(reservation));
    }

    @Test
    void updateThrowsWhenReservationIsNull() {
        reservationDbService.update(null);
        doThrow(NullPointerException.class).when(reservationDbService).update(null);

        assertThrows(NullPointerException.class,
                () -> reservationDbService.update(null));
    }

    @Test
    void validDeleteTest() {
        assertDoesNotThrow(() -> reservationDbService.delete(1));
    }

    @Test
    void deleteThrowsWhenIdIsZero() {
        reservationDbService.delete(0);
        doThrow(IllegalArgumentException.class).when(reservationDbService).delete(0);

        assertThrows(IllegalArgumentException.class,
                () -> reservationDbService.delete(0));
    }

    @Test
    void deleteThrowsWhenIdIsNegative() {
        reservationDbService.delete(-1);
        doThrow(IllegalArgumentException.class).when(reservationDbService).delete(-1);

        assertThrows(IllegalArgumentException.class,
                () -> reservationDbService.delete(-1));
    }

    @Test
    void deleteThrowsWhenReservationIsNotFound() {
        reservationDbService.delete(2);
        doThrow(NullPointerException.class).when(reservationDbService).delete(2);

        assertThrows(NullPointerException.class,
                () -> reservationDbService.delete(2));
    }

    @AfterEach
    void cleanup() {
        reservationDbService = null;
        room = null;
        hotel = null;
        user = null;
        reservation = null;
    }
}
