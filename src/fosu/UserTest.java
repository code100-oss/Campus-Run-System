package fosu;

public class UserTest {
    public static void main(String[] args) {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("123456");
        System.out.println("用户名: " + user.getUsername());
        System.out.println("密码: " + user.getPassword());

        // 使用 UserService 来修改密码并测试 changePassword 方法
        UserService service = new UserService();
        boolean changed = service.changePassword(user, "123456", "newpassword");
        System.out.println("修改结果: " + (changed ? "成功" : "失败"));
        System.out.println("修改后的密码: " + user.getPassword());
    }
}

