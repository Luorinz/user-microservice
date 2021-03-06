package com.climatetree.user.config;

import com.climatetree.user.enums.Constants;
import com.climatetree.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil implements Serializable {

  public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

  @Value("climatetree")
  private String secret;

  //retrieve username from jwt token
  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  //retrieve expiration date from jwt token
  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  //for retrieveing any information from token we will need the secret key
  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  //check if the token has expired
  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  //generate token for user
  public String generateToken(User userDetails) throws UnsupportedEncodingException {
    Map<String, Object> claims = new HashMap<>();
    claims.put(Constants.EMAIL.getStatusCode(), userDetails.getEmail());
    claims.put(Constants.USERID.getStatusCode(), userDetails.getUserId());
    claims.put(Constants.ROLE.getStatusCode(), userDetails.getRoleId());
    claims.put(Constants.NICKNAME.getStatusCode(), userDetails.getNickname());
    return doGenerateToken(claims, userDetails.getNickname());
  }

  private String doGenerateToken(Map<String, Object> claims, String subject)
      throws UnsupportedEncodingException {
    return
        Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(Date.from(Instant.ofEpochSecond(1584060612)))
            .setExpiration(Date.from(Instant.ofEpochSecond(1584064212))).setClaims(claims)
            .signWith(SignatureAlgorithm.HS256, secret.getBytes("UTF-8"))
            .compact();

  }

  //validate token
  public Boolean validateToken(String token, User userDetails) {
    final String username = getUsernameFromToken(token);
    return (username.equals(userDetails.getNickname()) && !isTokenExpired(token));
  }
}