package util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Random;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;


public class AuthenticationUtils {

    private static Random RANDOM = new SecureRandom();
    private static final String JWT_TOKEN_KEY = "supersecret";

    public static byte[] hash(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return hash;
    }

    public static byte[] hash(String password, byte[] salt)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(salt);
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return hash;
    }

    public static byte[] getRandomSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }

    public static String issueToken(String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_TOKEN_KEY);
            Date expirationDate = Date.from(ZonedDateTime.now().plusHours(24).toInstant());
            Date issuedAt = Date.from(ZonedDateTime.now().toInstant());
            return JWT.create().withIssuedAt(issuedAt).withExpiresAt(expirationDate).withClaim("email", email)
                    .withIssuer("jwtauth").sign(algorithm);
        } catch (JWTCreationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
