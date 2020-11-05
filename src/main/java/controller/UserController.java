package controller;

import dao.UserDAO;
import dao.UserDAOImpl;
import exception.UserAlreadyExistsException;
import model.RegisteringUser;
import model.Secured;
import model.User;
import util.AuthenticationUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;

@Path("users")
public class UserController {

    private UserDAO userDAO;

    public UserController() {
        this.userDAO = UserDAOImpl.getInstance();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(RegisteringUser user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Response response;
        try {
            userDAO.addUser(augmentUser(user));
            userDAO.addAddressToUser(user.getEmail(), "default", user.getAddress());
            // Clear password so it's not serialized on return...
            user.setClearTextPassword(null);
            response = Response.status(Response.Status.CREATED).entity(user).build();
        } catch (UserAlreadyExistsException | SQLException uae) {
            response = Response.status(Response.Status.CONFLICT).build();
        }
        return response;
    }

    @GET
    @Path("{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response get(@PathParam("email") String emailAddress) throws SQLException {
        Response response;
        User user = userDAO.getUser(emailAddress);
        if (user == null) {
            response = Response.status(Response.Status.NOT_FOUND).build();
        } else {
            response = Response.ok().entity(user).build();
        }
        return response;
    }

    private User augmentUser(RegisteringUser user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // Retrieve a random salt to be used when hashing an incoming password
        byte[] salt = AuthenticationUtils.getRandomSalt();
        // Hash the password
        byte[] hashPwd = AuthenticationUtils.hash(user.getClearTextPassword(), salt);
        user.setPassword(ByteBuffer.wrap(hashPwd));
        user.setSalt(ByteBuffer.wrap(salt));
//        user.setUserId(UUIDs.timeBased());
        return user;
    }
}
