package fosu;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A small UserService with in-memory user store and password hashing.
 * This is a lightweight improvement over the previous hardcoded check.
 */
public class UserService {
    // username -> hashedPassword
    private final Map<String, String> users = new ConcurrentHashMap<>();

    public UserService() {
        // seed default admin user (password: 123456)
        users.put("admin", hash("123456"));
    }

    public boolean register(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) return false;
        return users.putIfAbsent(username, hash(password)) == null;
    }

    public boolean login(String username, String password) {
        if (username == null || password == null) return false;
        String stored = users.get(username);
        if (stored == null) return false;
        return stored.equals(hash(password));
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        if (username == null || oldPassword == null || newPassword == null) return false;
        String stored = users.get(username);
        if (stored == null) return false;
        if (!stored.equals(hash(oldPassword))) return false;
        users.put(username, hash(newPassword));
        return true;
    }

    private String hash(String plain) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] d = md.digest(plain.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(d.length * 2);
            for (byte b : d) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // fallback (should not happen)
            throw new RuntimeException(e);
        }
    }

    /**
     * 尝试修改用户密码。验证旧密码是否匹配，并对新密码做最基本的校验（非空且长度>=6）。
     * @param user 待修改密码的用户
     * @param oldPassword 旧密码（用于校验）
     * @param newPassword 新密码
     * @return 修改成功返回 true，否则返回 false
     */
    public boolean changePassword(User user, String oldPassword, String newPassword) {
        if (user == null) return false;
        String current = user.getPassword();
        if (current == null || !current.equals(oldPassword)) {
            // 旧密码不匹配
            return false;
        }
        if (newPassword == null || newPassword.length() < 6) {
            // 新密码不符合最小长度要求
            return false;
        }
        user.setPassword(newPassword);
        return true;
    }
}

