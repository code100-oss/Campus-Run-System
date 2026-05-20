package fosu;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Improved UserService supporting studentId-based registration/login with
 * salted PBKDF2 password hashing, account lockout after repeated failures
 * and simple in-memory session tracking.
 */
public class UserService {
    private static final SecureRandom RNG = new SecureRandom();
    private static final String PBKDF2_ALGO = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256; // bits
    private static final int SALT_BYTES = 16;
    private static final int MAX_FAILED = 5;
    private static final long LOCK_MILLIS = 5 * 60 * 1000L; // 5 minutes lock

    private static class Record {
        final String saltBase64;
        final String hashBase64;
        volatile int failed;
        volatile long lockedUntil;

        Record(String saltBase64, String hashBase64) {
            this.saltBase64 = saltBase64;
            this.hashBase64 = hashBase64;
            this.failed = 0;
            this.lockedUntil = 0L;
        }
    }

    // studentId -> Record
    private final Map<String, Record> users = new ConcurrentHashMap<>();
    // simple session set (studentIds)
    private final Set<String> sessions = ConcurrentHashMap.newKeySet();

    public UserService() {
        // seed admin (studentId: admin, password: 123456)
        register("admin", "123456");
    }

    private static byte[] genSalt() {
        byte[] s = new byte[SALT_BYTES];
        RNG.nextBytes(s);
        return s;
    }

    private static String pbkdf2(char[] password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGO);
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean strongPassword(String pwd) {
        if (pwd == null) return false;
        String t = pwd.trim();
        if (t.length() < 8) return false; // min length
        boolean hasDigit = false, hasLetter = false;
        for (char c : t.toCharArray()) {
            if (Character.isDigit(c)) hasDigit = true;
            if (Character.isLetter(c)) hasLetter = true;
        }
        return hasDigit && hasLetter;
    }

    public boolean register(String studentId, String password) {
        if (studentId == null || studentId.trim().isEmpty()) return false;
        String sid = studentId.trim();
        // basic student id format: allow alphanumeric between 4 and 20 chars
        if (sid.length() < 4 || sid.length() > 20) return false;
        if (!strongPassword(password)) return false;

        byte[] salt = genSalt();
        String hash = pbkdf2(password.toCharArray(), salt);
        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        Record rec = new Record(saltBase64, hash);
        return users.putIfAbsent(sid, rec) == null;
    }

    public boolean login(String studentId, String password) {
        if (studentId == null || password == null) return false;
        Record rec = users.get(studentId);
        if (rec == null) return false;
        long now = System.currentTimeMillis();
        if (rec.lockedUntil > now) return false; // temporarily locked

        byte[] salt = Base64.getDecoder().decode(rec.saltBase64);
        String wanted = pbkdf2(password.toCharArray(), salt);
        if (constantTimeEquals(rec.hashBase64, wanted)) {
            // success
            rec.failed = 0;
            rec.lockedUntil = 0L;
            sessions.add(studentId);
            return true;
        } else {
            // failure
            rec.failed++;
            if (rec.failed >= MAX_FAILED) {
                rec.lockedUntil = now + LOCK_MILLIS;
            }
            return false;
        }
    }

    public boolean logout(String studentId) {
        if (studentId == null) return false;
        return sessions.remove(studentId);
    }

    public boolean isLoggedIn(String studentId) {
        return studentId != null && sessions.contains(studentId);
    }

    public boolean changePassword(String studentId, String oldPassword, String newPassword) {
        if (studentId == null || oldPassword == null || newPassword == null) return false;
        Record rec = users.get(studentId);
        if (rec == null) return false;
        byte[] salt = Base64.getDecoder().decode(rec.saltBase64);
        String oldHash = pbkdf2(oldPassword.toCharArray(), salt);
        if (!constantTimeEquals(rec.hashBase64, oldHash)) return false;
        if (!strongPassword(newPassword)) return false;

        byte[] newSalt = genSalt();
        String newHash = pbkdf2(newPassword.toCharArray(), newSalt);
        String newSaltBase64 = Base64.getEncoder().encodeToString(newSalt);
        users.put(studentId, new Record(newSaltBase64, newHash));
        return true;
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        if (a.length() != b.length()) return false;
        int res = 0;
        for (int i = 0; i < a.length(); i++) {
            res |= a.charAt(i) ^ b.charAt(i);
        }
        return res == 0;
    }
}

