package in.ac.vitap.cse1005.railmadad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {
  @GetMapping("/")
  public String home() {
    return "home.html";
  }

  @GetMapping("/customers")
  public String user() {
    return "customer_login.html";
  }

  @GetMapping("/officers")
  public String officer() {
    return "officer_login.html";
  }

  @GetMapping("/customerDashboard")
  public String customerDashboard() {
    return "customer_dashboard.html";
  }

  @GetMapping("/officerDashboard")
  public String officerDashboard() {
    return "officer_dashboard.html";
  }
}
