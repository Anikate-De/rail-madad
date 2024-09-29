package in.ac.vitap.cse1005.railmadad.domain.model;

import in.ac.vitap.cse1005.railmadad.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class UserClaims {
  private String id;
  private UserRole role;
}
