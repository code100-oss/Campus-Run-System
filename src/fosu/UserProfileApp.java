package fosu;

import java.util.Scanner;

/**
 * Simple command-line application to demonstrate user profile management.
 */
public class UserProfileApp {
    public static void main(String[] args) {
        UserProfileService service = new UserProfileService();
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                System.out.println("\n=== User Profile Management ===");
                System.out.println("1) Create profile");
                System.out.println("2) View profile");
                System.out.println("3) Update profile");
                System.out.println("4) Delete profile");
                System.out.println("0) Exit");
                System.out.print("Choose: ");
                String choice = scanner.nextLine().trim();
                if (choice.equals("0")) break;
                switch (choice) {
                    case "1":
                        System.out.print("username: ");
                        String u = scanner.nextLine().trim();
                        System.out.print("display name: ");
                        String d = scanner.nextLine().trim();
                        System.out.print("email: ");
                        String e = scanner.nextLine().trim();
                        System.out.print("phone: ");
                        String p = scanner.nextLine().trim();
                        UserProfile profile = new UserProfile(u, d, e, p);
                        service.saveProfile(profile);
                        System.out.println("saved: " + profile);
                        break;
                    case "2":
                        System.out.print("username: ");
                        String vu = scanner.nextLine().trim();
                        UserProfile found = service.getProfile(vu);
                        System.out.println(found == null ? "not found" : found);
                        break;
                    case "3":
                        System.out.print("username: ");
                        String uu = scanner.nextLine().trim();
                        System.out.print("new display name (blank to keep): ");
                        String nd = scanner.nextLine();
                        System.out.print("new email (blank to keep): ");
                        String ne = scanner.nextLine();
                        System.out.print("new phone (blank to keep): ");
                        String np = scanner.nextLine();
                        boolean ok = service.updateProfile(uu,
                                nd.isEmpty() ? null : nd,
                                ne.isEmpty() ? null : ne,
                                np.isEmpty() ? null : np);
                        System.out.println(ok ? "updated" : "update failed (invalid or not found)");
                        break;
                    case "4":
                        System.out.print("username: ");
                        String ru = scanner.nextLine().trim();
                        boolean removed = service.removeProfile(ru);
                        System.out.println(removed ? "removed" : "not found");
                        break;
                    default:
                        System.out.println("unknown choice");
                }
            }
        } finally {
            scanner.close();
        }
        System.out.println("bye");
    }
}

