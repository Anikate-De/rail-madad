package in.ac.vitap.cse1005.railmadad;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class RailMadadApplicationTests {

  @Test
  void contextLoads() {}
}
