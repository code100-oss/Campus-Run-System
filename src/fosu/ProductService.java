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

    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return listAllProducts();
        }
        String normalized = keyword.trim().toLowerCase();
        return store.values().stream()
                .filter(product -> containsIgnoreCase(product.getTitle(), normalized)
                        || containsIgnoreCase(product.getDescription(), normalized)
                        || containsIgnoreCase(product.getCategory(), normalized)
                        || containsIgnoreCase(product.getCondition(), normalized))
                .collect(Collectors.toList());
    }

    public List<Product> filterProducts(String category, Double minPrice, Double maxPrice, String condition, String status) {
        return store.values().stream()
                .filter(product -> category == null || category.isEmpty() || category.equalsIgnoreCase(product.getCategory()))
                .filter(product -> condition == null || condition.isEmpty() || condition.equalsIgnoreCase(product.getCondition()))
                .filter(product -> status == null || status.isEmpty() || status.equalsIgnoreCase(product.getStatus()))
                .filter(product -> minPrice == null || product.getPrice() >= minPrice)
                .filter(product -> maxPrice == null || product.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    public List<Product> searchAndFilter(String keyword, String category, Double minPrice, Double maxPrice, String condition, String status) {
        return searchProducts(keyword).stream()
                .filter(product -> category == null || category.isEmpty() || category.equalsIgnoreCase(product.getCategory()))
                .filter(product -> condition == null || condition.isEmpty() || condition.equalsIgnoreCase(product.getCondition()))
                .filter(product -> status == null || status.isEmpty() || status.equalsIgnoreCase(product.getStatus()))
                .filter(product -> minPrice == null || product.getPrice() >= minPrice)
                .filter(product -> maxPrice == null || product.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }
}
