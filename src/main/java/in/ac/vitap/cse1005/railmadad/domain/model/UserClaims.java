package in.ac.vitap.cse1005.railmadad.domain.model;

import in.ac.vitap.cse1005.railmadad.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Represents the claims of a user in the system.
 *
 * <p>This class is used to store and retrieve user claims such as user ID and role.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * UserClaims userClaims = UserClaims.builder()
 *   .id("userId")
 *   .role(UserRole.ADMIN)
 *   .build();
 * }</pre>
 */
@Builder
@AllArgsConstructor
@Getter
public class UserClaims {

  /** The unique identifier for the user. */
  private String id;

  /** The role of the user. */
  private UserRole role;
}
