package in.ac.vitap.cse1005.railmadad.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

/** Utility class for handling servlet operations. */
@UtilityClass
public class ServletUtils {

  final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

  /**
   * Writes a JSON response with the specified status and body.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * ServletUtils.writeResponse(response, HttpStatus.OK, responseBody);
   * }</pre>
   *
   * @param response the HttpServletResponse object
   * @param status the HTTP status to be set in the response
   * @param body the body to be written as JSON in the response
   * @throws IOException if an input or output exception occurs
   */
  public static void writeResponse(HttpServletResponse response, HttpStatus status, Object body)
      throws IOException {
    response.setStatus(status.value());
    response.getWriter().write(objectMapper.writeValueAsString(body));
    response.setContentType("application/json");
  }
}
