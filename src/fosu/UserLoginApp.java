package fosu;

import java.util.Scanner;

public class UserLoginApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();

        UserService userService = new UserService();
        boolean success = userService.login(username, password);
        if (success) {
            System.out.println("登录成功！");
        } else {
            System.out.println("用户名或密码错误！");
        }
    }
}

