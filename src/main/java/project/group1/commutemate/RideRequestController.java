package project.group1.commutemate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RideRequestController {

    @GetMapping("/ride-request/new")
    public String newRideRequest() {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Create Ride Request</title>
                </head>
                <body>
                    <h1>Create Ride Request</h1>

                    <form>
                        <label>Pickup Location:</label><br>
                        <input type="text" name="pickupLocation"><br><br>

                        <label>Destination:</label><br>
                        <input type="text" name="destination"><br><br>

                        <label>Date:</label><br>
                        <input type="date" name="date"><br><br>

                        <label>Time:</label><br>
                        <input type="time" name="time"><br><br>

                        <label>Notes:</label><br>
                        <textarea name="notes"></textarea><br><br>

                        <button type="submit">Create Request</button>
                    </form>

                    <p><a href="/rider/dashboard">Back to Rider Dashboard</a></p>
                </body>
                </html>
                """;
    }
}
