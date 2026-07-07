package project.group1.commutemate.model;

/**
 * How a member wants to use CommuteMate.
 * Mirrors the {@code commute_role} enum from the original design
 * (Supabase migration: 'driver', 'rider', 'both').
 */
public enum Role {
    DRIVER,
    RIDER,
    BOTH;

    /** Parse a role from user input, defaulting to BOTH for anything unexpected. */
    public static Role from(String value) {
        if (value == null) {
            return BOTH;
        }
        try {
            return Role.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return BOTH;
        }
    }

    /** Lower-case label used in the UI, e.g. "driver". */
    public String label() {
        return name().toLowerCase();
    }
}
