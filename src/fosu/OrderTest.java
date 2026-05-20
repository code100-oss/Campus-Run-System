package fosu;

import java.math.BigDecimal;
import java.util.List;

public class OrderTest {
    public static void main(String[] args) {
        OrderService service = new OrderService();

        String id1 = service.createOrder("p1", "buyer1", "seller1", new BigDecimal("100.00"));
        String id2 = service.createOrder("p2", "buyer2", "seller2", new BigDecimal("50.00"));

        System.out.println("Order 1: " + service.getOrder(id1));
        System.out.println("Order 2: " + service.getOrder(id2));

        List<Order> buyer1Orders = service.listOrdersByUser("buyer1");
        System.out.println("Buyer1 orders:");
        buyer1Orders.forEach(System.out::println);

        service.updateStatus(id1, "PAID");
        System.out.println("After update: " + service.getOrder(id1));
    }
}

