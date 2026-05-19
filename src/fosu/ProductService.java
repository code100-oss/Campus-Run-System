package fosu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ProductService {
    private final Map<String, Product> store = new ConcurrentHashMap<>();

    public String postProduct(Product product) {
        if (product == null || product.getSellerUsername() == null || product.getSellerUsername().isEmpty()) {
            throw new IllegalArgumentException("Product and seller username must not be null or empty");
        }
        if (product.getTitle() == null || product.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Product title must not be null or empty");
        }
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Product price must not be negative");
        }

        String id = UUID.randomUUID().toString();
        product.setId(id);
        if (product.getCreatedAt() == 0) {
            product.setCreatedAt(System.currentTimeMillis());
        }
        if (product.getStatus() == null || product.getStatus().isEmpty()) {
            product.setStatus("available");
        }
        store.put(id, product);
        return id;
    }

    public Product getProductById(String id) {
        if (id == null) return null;
        return store.get(id);
    }

    public boolean updateProduct(String id, String title, String description, Double price, String category, String condition, String status) {
        Product product = store.get(id);
        if (product == null) return false;
        if (title != null && !title.isEmpty()) product.setTitle(title);
        if (description != null) product.setDescription(description);
        if (price != null && price >= 0) product.setPrice(price);
        if (category != null) product.setCategory(category);
        if (condition != null) product.setCondition(condition);
        if (status != null) product.setStatus(status);
        return true;
    }

    public boolean removeProduct(String id) {
        return store.remove(id) != null;
    }

    public List<Product> listProductsBySeller(String sellerUsername) {
        if (sellerUsername == null) return Collections.emptyList();
        return store.values().stream()
                .filter(product -> sellerUsername.equals(product.getSellerUsername()))
                .collect(Collectors.toList());
    }

    public List<Product> listAllProducts() {
        return new ArrayList<>(store.values());
    }
}
