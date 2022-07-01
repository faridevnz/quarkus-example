package org.acme.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

public class Jwt {

    private static String SECRET = "mySuperSecureSecretVeryPowerfulAndNotPredictable";

    public static String generateToken(Long userId) {
        return JWT
                .create()
                .withIssuer("issuer@example.com")
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + 20000)) // 20 seconds
                .withArrayClaim("scopes", new String[]{"user.can_update", "user.can_create", "user.can_delete", "role.*"})
                .withClaim("user_id", userId)
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static String generateRefreshToken(Long userId) {
        return JWT
                .create()
                .withIssuer("issuer@example.com")
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400)) // 24 hours
                .withClaim("user_id", userId)
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static DecodedJWT validateToken(String jwt) throws JWTDecodeException {
        return JWT.decode(jwt);
    }

    public static Map<String, Claim> claimsFromToken(String jwt) {
        return Jwt.validateToken(jwt).getClaims();
    }

}
