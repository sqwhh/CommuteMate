package project.group1.commutemate.User;

import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import project.group1.commutemate.model.Profile;

/**
 * Resolves the signed-in member (if any) from the security context,
 * so public pages like the landing page can also reflect login state.
 */
@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** Empty when the visitor is not logged in. */
    public Optional<Profile> currentProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return userRepository.findByEmailIgnoreCase(auth.getName())
                .map(u -> new Profile(u.getEmail(), u.getFullName(), u.getRole(), 0, 0));
    }
}
