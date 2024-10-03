package in.ac.vitap.cse1005.railmadad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String block(){
        return "block";
    }

    @GetMapping("/customer_login")
    public String user() {
        return "customer_login.html";
    }
    @GetMapping("/officer_login")
    public String officer() {
        return "officer_login.html";
    }
    @GetMapping("/customer_login/user_dashboard")
    public String userDashboard() {
        return "user_dashboard";
    }
}
