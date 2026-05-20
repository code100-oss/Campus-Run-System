package fosu;

import java.util.List;
import java.util.Scanner;

public class OrderApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        OrderService service = new OrderService();
        while (true) {
            System.out.println("--- 二手交易订单管理 ---");
            System.out.println("1. 创建订单");
            System.out.println("2. 列出订单");
            System.out.println("3. 查看订单");
            System.out.println("4. 更新订单状态");
            System.out.println("5. 删除订单");
            System.out.println("0. 退出");
            System.out.print("选择: ");
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        System.out.print("buyerId: ");
                        String buyer = sc.nextLine().trim();
                        System.out.print("sellerId: ");
                        String seller = sc.nextLine().trim();
                        System.out.print("productId: ");
                        String product = sc.nextLine().trim();
                        System.out.print("price: ");
                        double price = Double.parseDouble(sc.nextLine().trim());
                        System.out.print("status: ");
                        String status = sc.nextLine().trim();
                        Order o = new Order(null, buyer, seller, product, price, status, 0L, 0L);
                        String id = service.createOrder(o);
                        System.out.println("创建成功，orderId=" + id);
                        break;
                    case "2":
                        List<Order> list = service.listOrders();
                        if (list.isEmpty()) System.out.println("无订单");
                        else list.forEach(System.out::println);
                        break;
                    case "3":
                        System.out.print("orderId: ");
                        String oid = sc.nextLine().trim();
                        Order found = service.getOrder(oid);
                        System.out.println(found == null ? "未找到订单" : found);
                        break;
                    case "4":
                        System.out.print("orderId: ");
                        String uid = sc.nextLine().trim();
                        Order uo = service.getOrder(uid);
                        if (uo == null) {
                            System.out.println("未找到订单");
                        } else {
                            System.out.print("new status: ");
                            String ns = sc.nextLine().trim();
                            uo.setStatus(ns);
                            service.updateOrder(uo);
                            System.out.println("更新成功");
                        }
                        break;
                    case "5":
                        System.out.print("orderId: ");
                        String did = sc.nextLine().trim();
                        boolean del = service.deleteOrder(did);
                        System.out.println(del ? "删除成功" : "删除失败或订单不存在");
                        break;
                    case "0":
                        System.out.println("退出");
                        sc.close();
                        return;
                    default:
                        System.out.println("无效选择");
                }
            } catch (Exception ex) {
                System.out.println("出错: " + ex.getMessage());
            }
            System.out.println();
        }
    }
}

