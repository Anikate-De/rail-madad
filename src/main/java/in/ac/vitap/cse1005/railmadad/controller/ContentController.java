package in.ac.vitap.cse1005.railmadad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {
  @GetMapping("/")
  public String home() {
    return "home";
  }

  @GetMapping("/customers")
  public String user() {
    return "customer_login.html";
  }

  @GetMapping("/officers")
  public String officer() {
    return "officer_login.html";
  }

  @GetMapping("/customers/user_dashboard")
  public String userDashboard() {
    return "user_dashboard";
  }
}
