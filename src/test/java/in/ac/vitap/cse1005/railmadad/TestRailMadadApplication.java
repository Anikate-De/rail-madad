package in.ac.vitap.cse1005.railmadad;

import org.springframework.boot.SpringApplication;

public class TestRailMadadApplication {

  public static void main(String[] args) {
    SpringApplication.from(RailMadadApplication::main)
        .with(TestcontainersConfiguration.class)
        .run(args);
  }
}
