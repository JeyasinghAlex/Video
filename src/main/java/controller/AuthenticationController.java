package controller;

import dao.UserDAO;
import dao.UserDAOImpl;
import model.User;
import org.apache.log4j.Logger;
import util.AuthenticationUtils;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;

public class AuthenticationController {

    private static final Logger log = Logger.getLogger(AuthenticationController.class);

    private final UserDAO userDAO;

    public AuthenticationController() {
        this.userDAO = UserDAOImpl.getInstance();
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("email") String email, @FormParam("password") String password)
            throws IOException, NoSuchAlgorithmException, SQLException {

        Response response = Response.status(Response.Status.FORBIDDEN).build();

        User user = userDAO.getUser(email);
        if (user != null) {
            byte[] salt = user.getSalt().array();
            byte[] hashedPassword = AuthenticationUtils.hash(password, salt);
            if (Arrays.equals(user.getPassword().array(), hashedPassword)) {
                log.debug("login successful -> username : {}" + email);
                String token = AuthenticationUtils.issueToken(email);
                response = Response.ok().header("authtoken", token).entity(user)
                        .build();
            }
        }
        return response;
    }
}
