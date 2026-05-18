package fosu;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory service for managing user profiles.
 * This is a minimal implementation for demo/testing purposes.
 */
public class UserProfileService {
    // simple in-memory store keyed by username
    private final Map<String, UserProfile> store = new ConcurrentHashMap<>();

    /**
     * Create or save a profile.
     */
    public void saveProfile(UserProfile profile) {
        if (profile == null || profile.getUsername() == null) {
            throw new IllegalArgumentException("profile and username must not be null");
        }
        store.put(profile.getUsername(), profile);
    }

    /**
     * Get profile by username, or null if not found.
     */
    public UserProfile getProfile(String username) {
        if (username == null) return null;
        return store.get(username);
    }

    /**
     * Update profile fields (displayName, email, phone). Returns true if updated.
     */
    public boolean updateProfile(String username, String displayName, String email, String phone) {
        UserProfile p = store.get(username);
        if (p == null) return false;
        // basic validation: email contains @ if provided
        if (email != null && !email.isEmpty() && !email.contains("@")) {
            return false;
        }
        if (displayName != null) p.setDisplayName(displayName);
        if (email != null) p.setEmail(email);
        if (phone != null) p.setPhone(phone);
        return true;
    }

    /**
     * Remove profile by username.
     */
    public boolean removeProfile(String username) {
        return store.remove(username) != null;
    }
}

