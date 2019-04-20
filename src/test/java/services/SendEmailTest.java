package services;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import database.Database;
import models.Hotel;
import models.Reservation;
import models.Room;
import models.User;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;

import static org.junit.Assert.assertEquals;

public class SendEmailTest {
    private Reservation reservation;
    private Room room;
    private User user;
    private Hotel hotel;

    @BeforeEach
    public void init() {
        hotel = new Hotel("Sample name", new LocalTime(8), new LocalTime(23));
        room = new Room(hotel, 200, 2);
        user = new User("test@test.com");
        reservation = new Reservation(new DateTime(2019, 5, 5, 11, 0),
                new DateTime(2019, 5, 6, 11, 0),
                user, room);
    }

    @Test
    public void sendTest() {
        GreenMail greenMail = new GreenMail();
        greenMail.start();

        SendEmail.send(reservation, user.getEmail(), "bookingsystem@bookingsystem.com", "localhost");

        assertEquals("Your reservation ID is " + reservation.getIdentificator() + ".",
                GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]));
        greenMail.stop();
    }

    @AfterEach
    public void cleanup() {
        reservation = null;
        room = null;
        user = null;
        hotel = null;
    }
}
