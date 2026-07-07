package project.group1.commutemate.controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import project.group1.commutemate.model.Profile;
import project.group1.commutemate.model.Role;

/**
 * Base class for pages that live behind the (future) login wall.
 *
 * <p>Real authentication is Epic 1 and not built yet, so for now every
 * signed-in page shares one demo profile. Exposing it as a {@code @ModelAttribute}
 * means the shared navigation fragment can always render the member's name.</p>
 */
public abstract class AuthenticatedController {

    @ModelAttribute("authenticated")
    public boolean authenticated() {
        return true;
    }

    @ModelAttribute("profile")
    public Profile currentProfile() {
        return new Profile("alex_chen@sfu.ca", "Alex Chen", Role.BOTH, 240, 82);
    }
}
