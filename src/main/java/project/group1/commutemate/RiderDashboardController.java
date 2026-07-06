package project.group1.commutemate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RiderDashboardController {

    @GetMapping("/rider/dashboard")
    public String riderDashboard() {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Rider Dashboard</title>
                </head>
                <body>
                    <h1>Rider Dashboard</h1>
                    <p>Welcome to your rider dashboard.</p>

                    <h2>Ride Options</h2>
                    <ul>
                        <li><a href="/rides">View Available Rides</a></li>
                        <li><a href="/ride-request/new">Create Ride Request</a></li>
                    </ul>
                </body>
                </html>
                """;
    }
}
