package database;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import models.*;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.LocalTime;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Database {
    private HashMap<Integer, Hotel> hotels;
    private HashMap<Integer, Reservation> reservations;
    private HashMap<Integer, Room> rooms;
    private HashMap<Integer, User> users;

    private Integer nextHotelId = 0;
    private Integer nextReservationId = 0;
    private Integer nextRoomId = 0;
    private Integer nextUserId = 0;

    public HashMap<Integer, Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(HashMap<Integer, Hotel> hotels) {
        this.hotels = hotels;
    }

    public HashMap<Integer, Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(HashMap<Integer, Reservation> reservations) {
        this.reservations = reservations;
    }

    public HashMap<Integer, Room> getRooms() {
        return rooms;
    }

    public void setRooms(HashMap<Integer, Room> rooms) {
        this.rooms = rooms;
    }

    public HashMap<Integer, User> getUsers() {
        return users;
    }

    public void setUsers(HashMap<Integer, User> users) {
        this.users = users;
    }

    public Integer getNextHotelId() {
        nextHotelId++;
        return nextHotelId;
    }

    public Integer getNextReservationId() {
        nextReservationId++;
        return nextReservationId;
    }

    public Integer getNextRoomId() {
        nextRoomId++;
        return nextRoomId;
    }

    public Integer getNextUserId() {
        nextUserId++;
        return nextUserId;
    }

    public Database() {
        hotels = new HashMap<>();
        reservations = new HashMap<>();
        rooms = new HashMap<>();
        users = new HashMap<>();
    }

    /** saving database to the file */
    public int serializeDatabase(String filename) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.writeValue(new File("target/" + filename + ".json"), this);

        return 0;
    }

    /** loading database from file */
    public static Database deserializeDatabase(String filename) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());

        return objectMapper.readValue(new File("target/" + filename + ".json"), Database.class);
    }
}
