package fosu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class OrderService {
    private final Map<String, Order> store = new ConcurrentHashMap<>();

    public String createOrder(String productId, String buyerId, String sellerId, BigDecimal amount) {
        String id = UUID.randomUUID().toString();
        Order o = new Order(id, productId, buyerId, sellerId, amount);
        store.put(id, o);
        return id;
    }

    public Order getOrder(String id) {
        return store.get(id);
    }

    public List<Order> listOrdersByUser(String userId) {
        if (userId == null) return new ArrayList<>(store.values());
        return store.values().stream()
                .filter(o -> userId.equals(o.getBuyerId()) || userId.equals(o.getSellerId()))
                .collect(Collectors.toList());
    }

    public boolean updateStatus(String id, String status) {
        Order o = store.get(id);
        if (o == null) return false;
        o.setStatus(status);
        return true;
    }

    public boolean removeOrder(String id) {
        return store.remove(id) != null;
    }

    public List<Order> listAll() {
        return new ArrayList<>(store.values());
    }
}

