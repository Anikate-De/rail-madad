package in.ac.vitap.cse1005.railmadad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RailMadadApplication {

  public static void main(String[] args) {
    SpringApplication.run(RailMadadApplication.class, args);
  }
}
