package in.ac.vitap.cse1005.railmadad.domain;

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
