package fosu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Simple in-memory product service for demo purposes.
 */
public class ProductService {
	private final Map<String, Product> store = new ConcurrentHashMap<>();

	/**
	 * Add a product. If id exists, it will be overwritten.
	 */
	public void addProduct(Product p) {
		if (p == null || p.getId() == null) throw new IllegalArgumentException("product and id must not be null");
		store.put(p.getId(), p);
	}

	public Product getProduct(String id) {
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

	public List<Product> listProductsBySeller(String sellerUsername) {
		// When sellerUsername is null we treat it as "all sellers" and return
		// a copy of the current product list. Returning a copy avoids accidental
		// modifications of the internal store by callers.
		if (sellerUsername == null) return new ArrayList<>(store.values());
		return store.values().stream()
				.filter(p -> sellerUsername.equals(p.getSellerUsername()))
				.collect(Collectors.toList());
	}

	/**
	 * Return a copy of all products currently stored.
	 */
	public List<Product> listAllProducts() {
		return new ArrayList<>(store.values());
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

