package in.ac.vitap.cse1005.railmadad.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

@UtilityClass
public class ServletUtils {
  final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

  public static void writeResponse(HttpServletResponse response, HttpStatus status, Object body)
      throws IOException {
    response.setStatus(status.value());
    response.getWriter().write(objectMapper.writeValueAsString(body));
    response.setContentType("application/json");
  }
}
