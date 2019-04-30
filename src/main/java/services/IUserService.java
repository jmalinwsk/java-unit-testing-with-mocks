package services;

import models.Room;
import models.User;

import java.util.HashMap;

public interface IUserService {
    void add(User user);
    User get(int id);
    HashMap<Integer, Room> get();
    void update(User user);
    void delete(int id);
}
