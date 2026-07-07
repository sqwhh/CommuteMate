package project.group1.commutemate.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;

import project.group1.commutemate.User.User;
import project.group1.commutemate.User.UserRepository;
import project.group1.commutemate.model.Profile;

/**
 * Base class for pages behind the login wall (Epic 1).
 *
 * <p>Exposes the signed-in member as a {@code @ModelAttribute} so the shared
 * navigation fragment can render their name, email, and role-appropriate tabs.
 * Points and eco-score stay at their starting values until Epic 4 lands.</p>
 */
public abstract class AuthenticatedController {

    private final UserRepository userRepository;

    protected AuthenticatedController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute("authenticated")
    public boolean authenticated() {
        return true;
    }

    @ModelAttribute("profile")
    public Profile currentProfile(Authentication authentication) {
        User user = userRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new IllegalStateException(
                        "Authenticated user not found: " + authentication.getName()));
        return new Profile(user.getEmail(), user.getFullName(), user.getRole(), 0, 0);
    }
}
