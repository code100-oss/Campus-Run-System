package fosu;

import java.util.Scanner;

public class UserLoginApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService();
        String current = null;
        while (true) {
            System.out.println("--- 学生账号管理 ---");
            System.out.println("1. 注册 (学号 + 密码)");
            System.out.println("2. 登录");
            System.out.println("3. 退出登录");
            System.out.println("4. 修改密码");
            System.out.println("0. 退出程序");
            System.out.print("选择: ");
            String c = scanner.nextLine().trim();
            if (c.equals("1")) {
                System.out.print("学号: ");
                String sid = scanner.nextLine().trim();
                System.out.print("密码(至少8位, 包含字母和数字): ");
                String pwd = scanner.nextLine();
                boolean ok = userService.register(sid, pwd);
                System.out.println(ok ? "注册成功" : "注册失败(可能学号已存在或密码不符合要求)");
            } else if (c.equals("2")) {
                System.out.print("学号: ");
                String sid = scanner.nextLine().trim();
                System.out.print("密码: ");
                String pwd = scanner.nextLine();
                boolean ok = userService.login(sid, pwd);
                if (ok) {
                    current = sid;
                    System.out.println("登录成功: " + sid);
                } else {
                    System.out.println("登录失败: 学号或密码错误，或账号被锁定");
                }
            } else if (c.equals("3")) {
                if (current == null) {
                    System.out.println("当前没有登录用户");
                } else {
                    userService.logout(current);
                    System.out.println("已退出: " + current);
                    current = null;
                }
            } else if (c.equals("4")) {
                if (current == null) {
                    System.out.println("请先登录");
                } else {
                    System.out.print("旧密码: ");
                    String oldp = scanner.nextLine();
                    System.out.print("新密码: ");
                    String newp = scanner.nextLine();
                    boolean ok = userService.changePassword(current, oldp, newp);
                    System.out.println(ok ? "修改成功" : "修改失败(旧密码错误或新密码不满足要求)");
                }
            } else if (c.equals("0")) {
                System.out.println("退出程序");
                break;
            } else {
                System.out.println("无效选择");
            }
            System.out.println();
        }
        scanner.close();
    }
}

