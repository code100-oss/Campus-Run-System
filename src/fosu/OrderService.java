package fosu;

import java.util.*;

public class OrderService {
    private final Map<String, Order> orders = new HashMap<>();

    public synchronized String createOrder(Order order) {
        String id = order.getOrderId();
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
            order.setOrderId(id);
        }
        long now = System.currentTimeMillis();
        order.setCreatedAt(now);
        order.setUpdatedAt(now);
        orders.put(id, order);
        return id;
    }

    public synchronized Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    public synchronized boolean updateOrder(Order order) {
        String id = order.getOrderId();
        if (id == null || !orders.containsKey(id)) return false;
        order.setUpdatedAt(System.currentTimeMillis());
        orders.put(id, order);
        return true;
    }

    public synchronized boolean deleteOrder(String orderId) {
        return orders.remove(orderId) != null;
    }

    public synchronized List<Order> listOrders() {
        return new ArrayList<>(orders.values());
    }
}

