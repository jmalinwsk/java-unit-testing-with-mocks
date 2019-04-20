package utils;

import models.Hotel;
import models.Reservation;
import models.Room;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ReservationUtilsTest {
    private Reservation r1;
    private Reservation r2;

    @BeforeEach
    public void init() {
        r1 = new Reservation();
        r2 = new Reservation();
    }

    @Test
    @DisplayName("returns true if date in reservation has minutes in start date")
    public void hasMinutesInDateTest() {
        r1 = new Reservation(
                new DateTime(2019, 4, 4, 10, 1),
                new DateTime(2019, 4, 5, 10, 0),
                null, null);

        assertThat(ReservationUtils.hasMinutesInDate(r1)).isTrue();
    }

    @Test
    @DisplayName("returns true if date in reservation has minutes in end date")
    public void hasMinutesInDate2Test() {
        r1 = new Reservation(
                new DateTime(2019, 4, 4, 10, 0),
                new DateTime(2019, 4, 5, 10, 1),
                null, null);

        assertThat(ReservationUtils.hasMinutesInDate(r1)).isTrue();
    }

    @Test
    @DisplayName("returns false if date in reservation hasn't minutes")
    public void hasMinutesInDate3Test() {
        r1 = new Reservation(
                new DateTime(2019, 4, 4, 10, 0),
                new DateTime(2019, 4, 5, 10, 0),
                null, null);

        assertThat(ReservationUtils.hasMinutesInDate(r1)).isEqualTo(false);
    }

    @Test
    @DisplayName("checks if date of first reservation is contained in " +
            "second reservation and returns true")
    public void isContainedInTest() {
        r1 = new Reservation(
                new DateTime(2019, 4, 4, 10, 0),
                new DateTime(2019, 4, 5, 10, 0),
                null, null);
        r2 = new Reservation(
                new DateTime(2019, 4, 3, 20, 0),
                new DateTime(2019, 4, 6, 20, 0),
                null, null);

        assertTrue(ReservationUtils.isContainedIn(r1, r2));
    }

    @Test
    @DisplayName("checks if date of first reservation is contained in " +
            "second reservation and returns false")
    public void isContainedIn2Test() {
        r1 = new Reservation(
                new DateTime(2019, 4, 10, 10, 0),
                new DateTime(2019, 4, 11, 10, 0),
                null, null);
        r2 = new Reservation(
                new DateTime(2019, 4, 4, 20, 0),
                new DateTime(2019, 4, 5, 20, 0),
                null, null);

        assertThat(ReservationUtils.hasMinutesInDate(r1)).isFalse();
    }

    @Test
    @DisplayName("checks if date of first reservation is equal to date in " +
            "second reservation and returns true")
    public void isEqualToTest() {
        r1 = new Reservation(
                new DateTime(2019, 4, 10, 10, 0),
                new DateTime(2019, 4, 11, 10, 0),
                null, null);
        r2 = new Reservation(
                new DateTime(2019, 4, 10, 10, 0),
                new DateTime(2019, 4, 11, 10, 0),
                null, null);

        assertThat(ReservationUtils.isEqualTo(r1, r2), equalTo(true));
    }

    @Test
    @DisplayName("checks if date of first reservation is equal to date in " +
            "second reservation and returns false")
    public void isEqualTo2Test() {
        r1 = new Reservation(
                new DateTime(2019, 4, 10, 10, 0),
                new DateTime(2019, 4, 11, 10, 0),
                null, null);
        r2 = new Reservation(
                new DateTime(2019, 4, 10, 10, 0),
                new DateTime(2019, 4, 11, 11, 0),
                null, null);

        assertThat(ReservationUtils.isEqualTo(r1, r2), not(true));
    }

    @Test
    @DisplayName("checks if given room from 1st reservation is in the same hotel " +
            "as room drom 2nd reservation and returns true")
    public void ifRoomIsInTheSameHotelTest() {
        Hotel hotel = new Hotel("Example name", new LocalTime(8), new LocalTime(22));
        hotel.setId(1);
        Room room1 = new Room(hotel, 2, 2);
        room1.setId(1);

        r1 = new Reservation(
                new DateTime(2019, 4, 10, 10, 0),
                new DateTime(2019, 4, 11, 10, 0),
                null, room1);
        r2 = new Reservation(
                new DateTime(2019, 4, 10, 10, 0),
                new DateTime(2019, 4, 11, 11, 0),
                null, room1);

        assertThat(ReservationUtils.ifRoomIsInTheSameHotel(r1, r2), is(true));
    }

    @Test
    @DisplayName("checks if given room from 1st reservation is in the same hotel " +
            "as room from 2nd reservation and returns false")
    public void ifRoomIsInTheSameHotel2Test() {
        Hotel hotel = new Hotel("Example name", new LocalTime(8), new LocalTime(22));
        Room room1 = new Room(hotel, 2, 2);
        Room room2 = new Room(hotel, 3, 2);
        hotel.setId(1);
        room1.setId(1);
        room2.setId(2);
        r1 = new Reservation(
                new DateTime(2019, 4, 10, 10, 0),
                new DateTime(2019, 4, 11, 10, 0),
                null, room1);
        r2 = new Reservation(
                new DateTime(2019, 4, 10, 10, 0),
                new DateTime(2019, 4, 11, 11, 0),
                null, room2);

        assertThat(ReservationUtils.ifRoomIsInTheSameHotel(r1, r2), is(false));
    }

    @Test
    @DisplayName("checks if given room from 1st reservation is in the same hotel " +
            "as room from 2nd reservation and returns false")
    public void ifRoomIsInTheSameHotel3Test() {
        Hotel hotel1 = new Hotel("Example name", new LocalTime(8), new LocalTime(22));
        Hotel hotel2 = new Hotel("Example name 2", new LocalTime(8), new LocalTime(22));
        Room room1 = new Room(hotel1, 2, 2);
        Room room2 = new Room(hotel2, 3, 2);
        hotel1.setId(1);
        hotel2.setId(2);
        room1.setId(1);
        room2.setId(2);
        r1 = new Reservation(
                new DateTime(2019, 4, 10, 10, 0),
                new DateTime(2019, 4, 11, 10, 0),
                null, room1);
        r2 = new Reservation(
                new DateTime(2019, 4, 10, 10, 0),
                new DateTime(2019, 4, 11, 11, 0),
                null, room2);

        assertFalse(ReservationUtils.ifRoomIsInTheSameHotel(r1, r2));
    }

    @Test
    @DisplayName("checks if date of first reservation has a product of sets with " +
            "date of second reservation and returns true")
    public void ifDatesHaveAnIntersectTest() {
        r1 = new Reservation(
                new DateTime(2019, 4, 10, 10, 0),
                new DateTime(2019, 4, 15, 10, 0),
                null, null);
        r2 = new Reservation(
                new DateTime(2019, 4, 9, 10, 0),
                new DateTime(2019, 4, 13, 10, 0),
                null, null);

        assertTrue(ReservationUtils.ifDatesHaveAnIntersect(r1, r2));
    }

    @Test
    @DisplayName("checks if date of first reservation has a product of sets with " +
            "date of second reservation and returns true")
    public void ifDatesHaveAnIntersect2Test() {
        r1 = new Reservation(
                new DateTime(2019, 4, 10, 10, 0),
                new DateTime(2019, 4, 15, 10, 0),
                null, null);
        r2 = new Reservation(
                new DateTime(2019, 4, 16, 10, 0),
                new DateTime(2019, 4, 20, 10, 0),
                null, null);

        assertFalse(ReservationUtils.ifDatesHaveAnIntersect(r1, r2));
    }

    @AfterEach
    public void cleanup() {
        r1 = null;
        r2 = null;
    }
}
