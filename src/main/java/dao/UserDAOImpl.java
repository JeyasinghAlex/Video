package dao;

import exception.UserAlreadyExistsException;
import exception.UserDoesNotMatchException;
import model.Address;
import model.User;
import util.ConfigUtil;

import java.nio.ByteBuffer;
import java.sql.*;
import java.util.Properties;

public class UserDAOImpl implements UserDAO {

    private static UserDAOImpl instance;
    private static Connection conn;

    private UserDAOImpl() {
        try {
            Properties properties = ConfigUtil.loadProperty();
            String url = properties.getProperty("mysql.CONNECTION_URL");
            String userName = properties.getProperty("mysql.username");
            String password = properties.getProperty("mysql.password");
            String driverName = properties.getProperty("mysql.CONNECTION_DRIVER");
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, userName, password);
        } catch (ClassNotFoundException | SQLException | NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public static UserDAOImpl getInstance() {
        if (instance == null) {
            instance = new UserDAOImpl();
        }
        return instance;
    }

    @Override
    public boolean addUser(User newUser) throws UserAlreadyExistsException, SQLException {
        final String query = "SELECT * from user WHERE email = ?";
        final PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, newUser.getEmail());
        final ResultSet rs = ps.executeQuery();
        if (!rs.isBeforeFirst()) {
            throw new UserAlreadyExistsException("Could not save user. A duplicate already exists");
        }
        return true;
    }

    @Override
    public User getUser(String email) throws SQLException {
        final String query = "SELECT * from user WHERE email = ?";
        final PreparedStatement ps = conn.prepareStatement(query);
        final ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            User user = new User();
            user.setEmail(rs.getString("email"));
            user.setFirstName(rs.getString("fname"));
            user.setLastName(rs.getString("lname"));
            byte[] pwd = rs.getBytes("password");
            user.setPassword(ByteBuffer.wrap(pwd));
            byte[] salt = rs.getBytes("salt");
            user.setSalt(ByteBuffer.wrap(salt));
            //userId
            //phoneNumber
            //address
            return user;
        }
        return null;
    }

    @Override
    public boolean addAddressToUser(String email, String addressName, Address newAddress) {
        return false;
    }

    @Override
    public boolean updateUser(User updatedUser) throws UserDoesNotMatchException {
        return false;
    }
}
