package controller;

import dao.UserDAO;
import dao.UserDAOImpl;
import model.Secured;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import model.User;
import util.UserSession;

@Provider
@Secured
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String JWT_TOKEN_KEY = "supersecret";
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }
        String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();

        try {
            isValidateToken(token);
        } catch (Exception e) {
            abortWithUnauthorized(requestContext);
        }
    }

    private boolean isTokenBasedAuthentication(String authorizationHeader) {
        return authorizationHeader != null
                && authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME).build());
    }

    private boolean isValidateToken(String token) {
        try {
            if (token != null) {
                Algorithm algorithm = Algorithm.HMAC256(JWT_TOKEN_KEY);
                JWTVerifier verifier = JWT.require(algorithm).withIssuer("jwtauth").build();
                DecodedJWT jwt = verifier.verify(token);
                Claim email = jwt.getClaim("email");
                UserDAO dao = UserDAOImpl.getInstance();
                User user = dao.getUser(email.asString());
                if (user != null) {
                    UserSession.setUser(user);
                    return true;
                }
            }
        } catch (JWTVerificationException | SQLException e) {
            e.printStackTrace();
        }
        throw new SecurityException("Invalid user/password");
    }
}
