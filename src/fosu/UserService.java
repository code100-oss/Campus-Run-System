package fosu;

public class UserService {
    // 简单模拟用户登录校验
    public boolean login(String username, String password) {
        // 实际项目应查数据库，这里仅做演示
        return "admin".equals(username) && "123456".equals(password);
    }
}

