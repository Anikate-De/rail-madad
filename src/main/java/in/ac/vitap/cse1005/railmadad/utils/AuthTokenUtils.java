package in.ac.vitap.cse1005.railmadad.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthTokenUtils {
  private static final SecretKey key = Jwts.SIG.HS256.key().build();

  public static String generateTokenWithId(String id, long expirationTimeMillis) {
    return Jwts.builder()
        .id(id)
        .expiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
        .issuedAt(new Date(System.currentTimeMillis()))
        .signWith(key)
        .compact();
  }

  public static String getIdFromToken(String token) {
    Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    if (claims.getExpiration().before(new Date())) {
      throw new ExpiredJwtException(null, claims, "Token expired");
    }
    return claims.getId();
  }
}
