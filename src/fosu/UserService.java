package fosu;

public class UserService {
    // 简单模拟用户登录校验
    public boolean login(String username, String password) {
        // 实际项目应查数据库，这里仅做演示
        return "admin".equals(username) && "123456".equals(password);
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

