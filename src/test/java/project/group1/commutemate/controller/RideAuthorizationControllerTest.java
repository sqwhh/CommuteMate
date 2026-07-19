package project.group1.commutemate.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import project.group1.commutemate.User.CurrentUserService;
import project.group1.commutemate.model.Profile;
import project.group1.commutemate.model.Role;
import project.group1.commutemate.service.RideCoordinationService;
import project.group1.commutemate.service.RideService;

@WebMvcTest(controllers = {RideRequestController.class, RidesController.class})
@AutoConfigureMockMvc(addFilters = false)
class RideAuthorizationControllerTest {

    private static final String SIGNED_IN_EMAIL = "owner@sfu.ca";
    private static final String FORGED_EMAIL = "victim@sfu.ca";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CurrentUserService currentUserService;

    @MockitoBean
    private RideCoordinationService coordinationService;

    @MockitoBean
    private RideService rideService;

    @MockitoBean
    private Clock clock;

    private Profile signedInProfile;

    @BeforeEach
    void setUp() {
        signedInProfile = new Profile(
                SIGNED_IN_EMAIL, "Signed In Member", Role.BOTH, 0, 0);
        when(currentUserService.currentProfile()).thenReturn(Optional.of(signedInProfile));
    }

    @Test
    void confirmUsesSignedInProfileWhenEmailParameterIsForged() throws Exception {
        mockMvc.perform(post("/ride-requests/{requestId}/confirm", 41L)
                        .param("email", FORGED_EMAIL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/driver"));

        assertEquals(SIGNED_IN_EMAIL, signedInProfile.getEmail());
        verify(coordinationService).confirmRequest(eq(41L), same(signedInProfile));
    }

    @Test
    void rejectUsesSignedInProfileWhenEmailParameterIsForged() throws Exception {
        mockMvc.perform(post("/ride-requests/{requestId}/reject", 42L)
                        .param("email", FORGED_EMAIL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/driver"));

        assertEquals(SIGNED_IN_EMAIL, signedInProfile.getEmail());
        verify(coordinationService).rejectRequest(eq(42L), same(signedInProfile));
    }

    @Test
    void cancelUsesSignedInProfileWhenEmailParameterIsForged() throws Exception {
        mockMvc.perform(post("/ride-requests/{requestId}/cancel", 43L)
                        .param("email", FORGED_EMAIL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/rider"));

        assertEquals(SIGNED_IN_EMAIL, signedInProfile.getEmail());
        verify(coordinationService).cancelRequest(eq(43L), same(signedInProfile));
    }

    @Test
    void deleteRideUsesSignedInProfileWhenEmailParameterIsForged() throws Exception {
        mockMvc.perform(post("/rides/{rideId}/delete", 44L)
                        .param("email", FORGED_EMAIL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/driver"));

        assertEquals(SIGNED_IN_EMAIL, signedInProfile.getEmail());
        verify(coordinationService).deleteOwnedRide(eq(44L), same(signedInProfile));
    }
}
