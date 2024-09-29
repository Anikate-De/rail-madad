package in.ac.vitap.cse1005.railmadad.filter;

import static in.ac.vitap.cse1005.railmadad.utils.ServletUtils.writeResponse;

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
public class CustomerAuthFilter extends OncePerRequestFilter {

  private final AuthService authService;

  @Autowired
  public CustomerAuthFilter(AuthService authService) {
    this.authService = authService;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    return !path.matches("/complaints/*");
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest servletRequest,
      @NonNull HttpServletResponse servletResponse,
      @NonNull FilterChain filterChain)
      throws IOException {

    try {
      String customerId =
          authService.authenticateCustomer((servletRequest).getHeader("Authorization"));
      servletRequest.setAttribute("id", customerId);

      filterChain.doFilter(servletRequest, servletResponse);

    } catch (ExpiredJwtException expiredJwtException) {
      writeResponse(servletResponse, HttpStatus.UNAUTHORIZED, Map.of("message", "Token expired."));
    } catch (Exception e) {
      writeResponse(servletResponse, HttpStatus.UNAUTHORIZED, Map.of("message", "Unauthorized."));
    }
  }
}
