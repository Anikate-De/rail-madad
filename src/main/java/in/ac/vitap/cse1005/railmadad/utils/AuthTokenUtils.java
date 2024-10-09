package in.ac.vitap.cse1005.railmadad.utils;

import in.ac.vitap.cse1005.railmadad.domain.enums.UserRole;
import in.ac.vitap.cse1005.railmadad.domain.model.UserClaims;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.experimental.UtilityClass;

/** Utility class for handling JWT token operations. */
@UtilityClass
public class AuthTokenUtils {
  private static final SecretKey key = Jwts.SIG.HS256.key().build();

  /**
   * Generates a JWT token from the provided user claims.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * UserClaims userClaims = UserClaims.builder().id("123").role(UserRole.CUSTOMER).build();
   * String token = AuthTokenUtils.generateTokenFromUserClaims(userClaims, 3600000);
   * }</pre>
   *
   * @param userClaims the user claims to be included in the token
   * @param expirationTimeMillis the expiration time in milliseconds
   * @return the generated JWT token
   */
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

  /**
   * Extracts user claims from the provided JWT token.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * UserClaims userClaims = AuthTokenUtils.getUserClaimsFromToken(token);
   * }</pre>
   *
   * @param token the JWT token containing user claims
   * @return the extracted user claims
   * @throws ExpiredJwtException if the token has expired
   */
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
