package project.group1.commutemate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Renders the sign-in / sign-up page (Epic 1).
 *
 * <p>Submission is handled elsewhere: login POSTs to {@code /login}
 * (Spring Security form login, see {@code SecurityConfig}) and sign-up
 * POSTs to {@code /register} (see {@code RegisterController}).</p>
 */
@Controller
public class AuthController {

    @GetMapping("/auth")
    public String authPage(@RequestParam(defaultValue = "login") String mode,
                           @RequestParam(required = false) String error,
                           @RequestParam(required = false) String registered,
                           Model model) {
        model.addAttribute("authenticated", false);
        model.addAttribute("mode", "signup".equals(mode) ? "signup" : "login");
        if (error != null) {
            model.addAttribute("error", "Invalid email or password");
        }
        if (registered != null) {
            model.addAttribute("success", "Account created — log in with your SFU email.");
        }
        return "auth";
    }
}
