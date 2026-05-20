package fosu;

public class OrderServiceTest {
    public static void main(String[] args) {
        OrderService service = new OrderService();
        Order o = new Order(null, "buyer1", "seller1", "prod1", 99.99, "PENDING", 0L, 0L);
        String id = service.createOrder(o);
        if (service.getOrder(id) == null) throw new RuntimeException("create/get failed");
        o.setStatus("PAID");
        service.updateOrder(o);
        if (!"PAID".equals(service.getOrder(id).getStatus())) throw new RuntimeException("update failed");
        service.deleteOrder(id);
        if (service.getOrder(id) != null) throw new RuntimeException("delete failed");
        System.out.println("OrderService tests passed.");
    }
}

