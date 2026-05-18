package fosu;

public class UserProfileTest {
    public static void main(String[] args) {
        UserProfileService service = new UserProfileService();

        UserProfile p = new UserProfile("alice", "Alice A.", "alice@example.com", "1234567890");
        service.saveProfile(p);

        System.out.println("Saved profile: " + service.getProfile("alice"));

        boolean updated = service.updateProfile("alice", "Alice Adams", "alice@domain.com", null);
        System.out.println("Update result: " + updated);
        System.out.println("Profile after update: " + service.getProfile("alice"));

        boolean removed = service.removeProfile("alice");
        System.out.println("Removed: " + removed);
        System.out.println("Get after remove: " + service.getProfile("alice"));
    }
}

