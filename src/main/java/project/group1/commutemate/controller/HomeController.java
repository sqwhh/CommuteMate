package project.group1.commutemate.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import project.group1.commutemate.User.CurrentUserService;
import project.group1.commutemate.model.Profile;

/**
 * Public landing page for CommuteMate. Also visible while logged in,
 * so the nav must reflect the member's real session state.
 */
@Controller
public class HomeController {

    private final CurrentUserService currentUserService;

    public HomeController(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @GetMapping("/")
    public String landing(Model model) {
        Optional<Profile> profile = currentUserService.currentProfile();
        model.addAttribute("authenticated", profile.isPresent());
        profile.ifPresent(p -> model.addAttribute("profile", p));
        return "index";
    }
}
