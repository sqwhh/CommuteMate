package project.group1.commutemate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DriverDashboardController {

    @GetMapping("/driver/dashboard")
    public String driverDashboard() {
        return "driver-dashboard";
    }
}
