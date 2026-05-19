package fosu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ProductService {
    // simple in-memory store
    protected final Map<String, Product> store = new ConcurrentHashMap<>();

    public void addProduct(Product p) {
        if (p == null) throw new IllegalArgumentException("Product must not be null");
        if (p.getId() == null || p.getId().trim().isEmpty()) throw new IllegalArgumentException("Product id must not be null/empty");
        store.put(p.getId(), p);
    }

    public Product getProduct(String id) {
        if (id == null) return null;
        return store.get(id);
    }

    public boolean updateProduct(String id, String title, String description, BigDecimal price, String category, String condition) {
        Product p = store.get(id);
        if (p == null) return false;
        if (title != null) p.setTitle(title);
        if (description != null) p.setDescription(description);
        if (price != null) p.setPrice(price);
        if (category != null) p.setCategory(category);
        if (condition != null) p.setCondition(condition);
        return true;
    }

    public boolean removeProduct(String id) {
        return store.remove(id) != null;
    }

    public List<Product> searchByTitle(String keyword) {
        // if keyword is null or empty, return all products
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>(store.values());
        }
        String k = keyword.toLowerCase();
        return store.values().stream()
                .filter(p -> {
                    String t = p.getTitle();
                    return t != null && t.toLowerCase().contains(k);
                })
                .collect(Collectors.toList());
    }

    public List<Product> listProductsBySeller(String seller) {
        if (seller == null || seller.trim().isEmpty()) {
            return new ArrayList<>(store.values());
        }
        String s = seller.toLowerCase();
        return store.values().stream()
                .filter(p -> p.getSeller() != null && p.getSeller().toLowerCase().equals(s))
                .collect(Collectors.toList());
    }

    // safe read-only view
    public List<Product> listAll() {
        return Collections.unmodifiableList(new ArrayList<>(store.values()));
    }

    /**
     * Returns number of products currently stored.
     */
    public int getCount() {
        return store.size();
    }
}

