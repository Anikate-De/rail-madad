package in.ac.vitap.cse1005.railmadad.utils;

import in.ac.vitap.cse1005.railmadad.domain.enums.UserRole;
import in.ac.vitap.cse1005.railmadad.domain.model.UserClaims;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthTokenUtils {
  private static final SecretKey key = Jwts.SIG.HS256.key().build();

  public static String generateTokenFromUserClaims(
      UserClaims userClaims, long expirationTimeMillis) {
    return Jwts.builder()
        .id(userClaims.getId())
        .claim("role", userClaims.getRole().name())
        .expiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
        .issuedAt(new Date(System.currentTimeMillis()))
        .signWith(key)
        .compact();
  }

  public static UserClaims getUserClaimsFromToken(String token) {
    Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    if (claims.getExpiration().before(new Date())) {
      throw new ExpiredJwtException(null, claims, "Token expired");
    }
    return UserClaims.builder()
        .id(claims.getId())
        .role(UserRole.valueOf((String) claims.get("role")))
        .build();
  }
}
