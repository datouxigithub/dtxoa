package dtx.rbac.controller;

import java.util.List;

import dtx.rbac.bean.User;

public interface UserController {

    public User getUserById(String userId);

    public User getUserByAccount(String account);

    public List<User> getUsersByStatus(boolean status);

    public String addUser(User user);

    public boolean deleteUser(String userId);

    public boolean deleteUser(User user);

    public boolean updateUser(User user);

    public boolean updateLoginMessage(User user);

    public boolean isAdmin(String account);

    public boolean isAdmin(User user);

}
