package fosu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class UserLoginAppTest {
    public static void main(String[] args) {
        System.out.println("Running UserLoginApp tests...");

        boolean ok1 = runLoginTest("admin", "123456", "登录成功！");
        boolean ok2 = runLoginTest("admin", "wrongpw", "用户名或密码错误！");

        if (ok1 && ok2) {
            System.out.println("All tests passed.");
            System.exit(0);
        } else {
            System.out.println("Some tests failed.");
            System.exit(1);
        }
    }

    private static boolean runLoginTest(String username, String password, String expectedMessage) {
        String input = username + System.lineSeparator() + password + System.lineSeparator();

        InputStream inBackup = System.in;
        PrintStream outBackup = System.out;
        try {
            // Redirect System.in
            ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
            System.setIn(testIn);

            // Capture System.out
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos, true, "UTF-8");
            System.setOut(ps);

            // Run the application
            UserLoginApp.main(new String[0]);

            ps.flush();
            String output = baos.toString("UTF-8");

            boolean passed = output.contains(expectedMessage);
            System.setOut(outBackup);
            System.out.println("Test (" + username + "/" + password + ") -> expected: '" + expectedMessage + "' => " + (passed ? "PASS" : "FAIL"));
            if (!passed) {
                System.out.println("Full output:\n" + output);
            }
            return passed;
        } catch (Exception e) {
            System.setOut(outBackup);
            System.out.println("Exception during test: " + e);
            return false;
        } finally {
            // Restore System.in to avoid side effects
            try { System.setIn(inBackup); } catch (Exception ignored) {}
        }
    }
}

