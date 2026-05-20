package fosu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class OrderApp {
    public static void main(String[] args) {
        OrderService service = new OrderService();
        Scanner sc = new Scanner(System.in);
        try {
            while (true) {
                System.out.println("--- Order Management ---");
                System.out.println("1) Create order");
                System.out.println("2) List orders by user");
                System.out.println("3) Update order status");
                System.out.println("4) List all orders");
                System.out.println("5) Exit");
                System.out.print("Choose: ");
                String c = sc.nextLine().trim();
                if (c.equals("1")) {
                    System.out.print("Product ID: ");
                    String pid = sc.nextLine().trim();
                    System.out.print("Buyer ID: ");
                    String bid = sc.nextLine().trim();
                    System.out.print("Seller ID: ");
                    String sid = sc.nextLine().trim();
                    System.out.print("Amount: ");
                    String amt = sc.nextLine().trim();
                    BigDecimal amount = new BigDecimal(amt);
                    String id = service.createOrder(pid, bid, sid, amount);
                    System.out.println("Created order id: " + id);
                } else if (c.equals("2")) {
                    System.out.print("User ID: ");
                    String uid = sc.nextLine().trim();
                    List<Order> list = service.listOrdersByUser(uid);
                    list.forEach(System.out::println);
                } else if (c.equals("3")) {
                    System.out.print("Order ID: ");
                    String oid = sc.nextLine().trim();
                    System.out.print("New Status: ");
                    String st = sc.nextLine().trim();
                    boolean ok = service.updateStatus(oid, st);
                    System.out.println(ok ? "Updated" : "Order not found");
                } else if (c.equals("4")) {
                    service.listAll().forEach(System.out::println);
                } else if (c.equals("5")) {
                    break;
                } else {
                    System.out.println("Unknown option");
                }
            }
        } finally {
            sc.close();
        }
    }
}

