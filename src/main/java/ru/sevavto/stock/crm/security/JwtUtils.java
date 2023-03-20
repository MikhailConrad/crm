package ru.sevavto.stock.crm.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

@Component
public class JwtUtils {

    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final int FIVE_MIN = 1000 * 60 * 5;
    private static final int ONE_DAY = 1000 * 60 * 60 * 24;

    public String getUsernameFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return (String) claims.get("login");
    }

    public Date getCreatedDateFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getIssuedAt();
    }


    public Date getExpirationDateFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getExpiration();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject("auth")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + FIVE_MIN))
                .claim("login", userDetails.getUsername())
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject("auth")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ONE_DAY))
                .claim("login", userDetails.getUsername())
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())) && !isAccessTokenExpired(token);
    }

    private boolean isAccessTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())) && !isRefreshTokenExpired(token);
    }

    private boolean isRefreshTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }
}
