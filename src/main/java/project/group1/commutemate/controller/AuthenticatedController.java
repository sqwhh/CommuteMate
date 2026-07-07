package project.group1.commutemate.controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import project.group1.commutemate.User.CurrentUserService;
import project.group1.commutemate.model.Profile;

/**
 * Base class for pages behind the login wall (Epic 1).
 *
 * <p>Exposes the signed-in member as a {@code @ModelAttribute} so the shared
 * navigation fragment can render their name, email, and role-appropriate tabs.
 * Points and eco-score stay at their starting values until Epic 4 lands.</p>
 */
public abstract class AuthenticatedController {

    private final CurrentUserService currentUserService;

    protected AuthenticatedController(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @ModelAttribute("authenticated")
    public boolean authenticated() {
        return true;
    }

    @ModelAttribute("profile")
    public Profile currentProfile() {
        return currentUserService.currentProfile()
                .orElseThrow(() -> new IllegalStateException("No signed-in member on a protected page"));
    }
}
