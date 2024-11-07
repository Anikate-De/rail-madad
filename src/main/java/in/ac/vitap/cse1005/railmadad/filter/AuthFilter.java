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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter for handling authentication of requests.
 *
 * <p>This filter intercepts requests to check for valid authentication tokens.
 */
@Slf4j
@Component
public class AuthFilter extends OncePerRequestFilter {

  private final AuthService authService;

  /**
   * Constructs an AuthFilter with the specified AuthService.
   *
   * @param authService the service for handling authentication
   */
  @Autowired
  public AuthFilter(AuthService authService) {
    this.authService = authService;
  }

  /**
   * Determines whether the filter should not apply to the given request.
   *
   * @param request the HTTP request
   * @return true if the filter should not apply, false otherwise
   */
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    return !(path.startsWith("/complaints")
        || path.endsWith("/customer")
        || path.endsWith("/officer"));
  }

  /**
   * Filters the request to check for valid authentication.
   *
   * @param servletRequest the HTTP request
   * @param servletResponse the HTTP response
   * @param filterChain the filter chain
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest servletRequest,
      @NonNull HttpServletResponse servletResponse,
      @NonNull FilterChain filterChain)
      throws IOException {

    try {
      UserClaims userClaims = authService.authenticate((servletRequest).getHeader("Authorization"));
      servletRequest.setAttribute("id", userClaims.getId());
      servletRequest.setAttribute("user", userClaims.getUser());
      servletRequest.setAttribute("role", userClaims.getRole());

      filterChain.doFilter(servletRequest, servletResponse);

    } catch (ExpiredJwtException expiredJwtException) {
      writeResponse(servletResponse, HttpStatus.UNAUTHORIZED, Map.of("message", "Token expired."));
    } catch (Exception e) {
      log.error("Error while filtering auth: ", e);
      writeResponse(servletResponse, HttpStatus.UNAUTHORIZED, Map.of("message", "Unauthorized."));
    }
  }
}
