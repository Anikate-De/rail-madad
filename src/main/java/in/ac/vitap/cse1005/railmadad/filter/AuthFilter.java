package in.ac.vitap.cse1005.railmadad.filter;

import static in.ac.vitap.cse1005.railmadad.utils.ServletUtils.writeResponse;

import in.ac.vitap.cse1005.railmadad.domain.model.UserClaims;
import in.ac.vitap.cse1005.railmadad.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthFilter extends OncePerRequestFilter {

  private final AuthService authService;

  @Autowired
  public AuthFilter(AuthService authService) {
    this.authService = authService;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    return !path.startsWith("/complaints");
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest servletRequest,
      @NonNull HttpServletResponse servletResponse,
      @NonNull FilterChain filterChain)
      throws IOException {

    try {
      UserClaims userClaims = authService.authenticate((servletRequest).getHeader("Authorization"));
      servletRequest.setAttribute("id", userClaims.getId());
      servletRequest.setAttribute("role", userClaims.getRole());

      filterChain.doFilter(servletRequest, servletResponse);

    } catch (ExpiredJwtException expiredJwtException) {
      writeResponse(servletResponse, HttpStatus.UNAUTHORIZED, Map.of("message", "Token expired."));
    } catch (Exception e) {
      writeResponse(servletResponse, HttpStatus.UNAUTHORIZED, Map.of("message", "Unauthorized."));
    }
  }
}
