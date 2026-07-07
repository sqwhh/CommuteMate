package project.group1.commutemate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project.group1.commutemate.model.Role;

/**
 * Sign-in / sign-up (Epic 1).
 *
 * <p>There is no real identity provider wired up yet, so this validates the
 * SFU-email rule and password length, then sends the member to the appropriate
 * dashboard. The validation logic is ported from the prototype's auth page.</p>
 */
@Controller
public class AuthController {

    @GetMapping("/auth")
    public String authPage(@RequestParam(defaultValue = "login") String mode, Model model) {
        model.addAttribute("authenticated", false);
        model.addAttribute("mode", "signup".equals(mode) ? "signup" : "login");
        return "auth";
    }

    @PostMapping("/auth")
    public String submit(@RequestParam String mode,
                         @RequestParam String email,
                         @RequestParam String password,
                         @RequestParam(required = false) String name,
                         @RequestParam(required = false) String role,
                         Model model) {

        String cleanEmail = email == null ? "" : email.trim().toLowerCase();

        String error = validate(cleanEmail, password);
        if (error != null) {
            model.addAttribute("authenticated", false);
            model.addAttribute("mode", "signup".equals(mode) ? "signup" : "login");
            model.addAttribute("error", error);
            model.addAttribute("email", email);
            model.addAttribute("name", name);
            return "auth";
        }

        Role selected = Role.from(role);
        if ("signup".equals(mode) && selected == Role.DRIVER) {
            return "redirect:/dashboard/driver";
        }
        return "redirect:/dashboard/rider";
    }

    private String validate(String cleanEmail, String password) {
        if (!cleanEmail.endsWith("@sfu.ca")) {
            return "Please use your @sfu.ca email — CommuteMate is verified-students only.";
        }
        if (password == null || password.length() < 6) {
            return "Password must be at least 6 characters.";
        }
        return null;
    }
}
