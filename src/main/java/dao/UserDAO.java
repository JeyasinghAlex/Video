package dao;

import exception.UserAlreadyExistsException;
import exception.UserDoesNotMatchException;
import model.Address;
import model.User;

import java.sql.SQLException;

public interface UserDAO {

    boolean addUser(User newUser) throws UserAlreadyExistsException, SQLException;

    User getUser(String email) throws SQLException;

    boolean addAddressToUser(String email, String addressName, Address newAddress);

    boolean updateUser(User updatedUser) throws UserDoesNotMatchException;
}
