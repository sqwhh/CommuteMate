package project.group1.commutemate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Public landing page for CommuteMate.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String landing(Model model) {
        model.addAttribute("authenticated", false);
        return "index";
    }
}
