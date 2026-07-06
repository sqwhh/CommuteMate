package project.group1.commutemate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RiderDashboardController {

    @GetMapping("/rider/dashboard")
    public String riderDashboard() {
        return "rider-dashboard";
    }
}