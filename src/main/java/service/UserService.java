package service;

import model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class UserService {
    private static UserService instance;

    private UserService() {
    }

    // Singleton
    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    /* хранилище данных */
    private Map<Long, User> dataBase = Collections.synchronizedMap(new HashMap<>());
    /* счетчик id */
    private AtomicLong maxId = new AtomicLong(0);
    /* список авторизованных пользователей */
    private Map<Long, User> authMap = Collections.synchronizedMap(new HashMap<>());


    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        dataBase.forEach((id, user) -> list.add(user));
        return list;
    }

    public User getUserById(Long id) {
        return dataBase.getOrDefault(id, null);
    }

    public boolean addUser(User user) {
        if (isExistsThisUser(user)) {
            return false;
        }
        user.setId(maxId.incrementAndGet());
        dataBase.put(maxId.get(), user);
        return true;
    }

    public void deleteAllUser() {
        dataBase.clear();
    }

    public boolean isExistsThisUser(User user) {
        for (Map.Entry<Long, User> entry : dataBase.entrySet()) {
            if (entry.getValue().getEmail().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }

    public List<User> getAllAuth() {
        List<User> list = new ArrayList<>();
        authMap.forEach((id, user) -> list.add(user));
        return list;
    }

    public boolean authUser(User user) {
        for (Map.Entry<Long, User> entry : dataBase.entrySet()) {
            if (entry.getValue().getEmail().equals(user.getEmail())) {
                authMap.put(entry.getKey(), entry.getValue());
                return true;
            }
        }
        return false;
    }

    public void logoutAllUsers() {
        authMap.clear();
    }

    public boolean isUserAuthById(Long id) {
        return authMap.containsKey(id);
    }
}
