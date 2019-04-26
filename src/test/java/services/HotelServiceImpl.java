package services;

import models.Hotel;
import org.joda.time.LocalTime;

import javax.sql.DataSource;
import java.sql.*;

public class HotelServiceImpl implements IHotelService {
    private DataSource dataSource;

    public HotelServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(Hotel hotel) {
        try {
            if (hotel == null)
                throw new NullPointerException();

            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO hotel (id, name, openHour, closeHour) VALUES (?, ?, ?, ?)");
            preparedStatement.setInt(1, hotel.getId());
            preparedStatement.setString(2, hotel.getName());
            preparedStatement.setDate(3, Date.valueOf(hotel.getOpenHour().toString()));
            preparedStatement.setDate(4, Date.valueOf(hotel.getCloseHour().toString()));
            connection.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Hotel get(int id) {
        Hotel hotel = new Hotel();
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT id, name, openHour, closeHour FROM hotel WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.first())
                return null;
            hotel.setId(resultSet.getInt(1));
            hotel.setName(resultSet.getString(2));
            hotel.setOpenHour(LocalTime.parse(resultSet.getTime(3).toString()));
            hotel.setCloseHour(LocalTime.parse(resultSet.getTime(4).toString()));
            connection.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return hotel;
    }

    @Override
    public void update(Hotel hotel) {
        try {
            if (hotel == null)
                throw new NullPointerException();

            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE hotel SET " +
                            "name = ?," +
                            "openHour = ?," +
                            "closeHour = ?" +
                            "WHERE id = ?");

            preparedStatement.setString(1, hotel.getName());
            preparedStatement.setDate(2, Date.valueOf(hotel.getOpenHour().toString()));
            preparedStatement.setDate(3, Date.valueOf(hotel.getCloseHour().toString()));
            preparedStatement.setInt(4, hotel.getId());
            preparedStatement.executeUpdate();
            connection.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM hotel WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
