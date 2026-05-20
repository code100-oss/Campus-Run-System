package fosu;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
		if (p == null) throw new IllegalArgumentException("product must not be null");
		// Basic validation & sanitization
		String title = p.getTitle() == null ? "" : p.getTitle().trim();
		if (title.isEmpty() || title.length() > 200) throw new IllegalArgumentException("title required and must be <=200 chars");
		String description = p.getDescription() == null ? "" : p.getDescription().trim();
		if (description.length() > 2000) throw new IllegalArgumentException("description must be <=2000 chars");
		if (p.getPrice() == null) throw new IllegalArgumentException("price required");
		if (p.getPrice().compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("price must be non-negative");
		// normalize price to 2 decimals
		p.setPrice(p.getPrice().setScale(2, RoundingMode.HALF_UP));

		// generate id if missing
		if (p.getId() == null || p.getId().trim().isEmpty()) {
			p.setId(UUID.randomUUID().toString());
		}

		// Prevent accidental overwrite: require explicit remove before re-adding
		if (store.containsKey(p.getId())) {
			throw new IllegalArgumentException("product with same id already exists");
		}

		long now = System.currentTimeMillis();
		p.setCreatedAt(now);
		p.setUpdatedAt(now);
		if (p.getStatus() == null) p.setStatus("AVAILABLE");

		store.put(p.getId(), new Product(p)); // store a defensive copy
	}

	public Product getProduct(String id) {
		return store.get(id);
	}

	public boolean updateProduct(String id, String title, String description, BigDecimal price, String category, String condition) {
		Product p = store.get(id);
		if (p == null) return false;
		if (title != null) p.setTitle(title.trim());
		if (description != null) p.setDescription(description.trim());
		if (price != null) {
			if (price.compareTo(BigDecimal.ZERO) < 0) return false;
			p.setPrice(price.setScale(2, RoundingMode.HALF_UP));
		}
		if (category != null) p.setCategory(category.trim());
		if (condition != null) p.setCondition(condition.trim());
		p.setUpdatedAt(System.currentTimeMillis());
		store.put(id, new Product(p));
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
		// return defensive copies
		return store.values().stream().map(Product::new).collect(Collectors.toList());
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
				.map(Product::new)
				.collect(Collectors.toList());
    }

    public List<Product> filterProducts(String category, Double minPrice, Double maxPrice, String condition, String status) {
		BigDecimal min = minPrice == null ? null : BigDecimal.valueOf(minPrice).setScale(2, RoundingMode.HALF_UP);
		BigDecimal max = maxPrice == null ? null : BigDecimal.valueOf(maxPrice).setScale(2, RoundingMode.HALF_UP);
		return store.values().stream()
				.filter(product -> category == null || category.isEmpty() || category.equalsIgnoreCase(product.getCategory()))
				.filter(product -> condition == null || condition.isEmpty() || condition.equalsIgnoreCase(product.getCondition()))
				.filter(product -> status == null || status.isEmpty() || status.equalsIgnoreCase(product.getStatus()))
				.filter(product -> min == null || product.getPrice().compareTo(min) >= 0)
				.filter(product -> max == null || product.getPrice().compareTo(max) <= 0)
				.map(Product::new)
				.collect(Collectors.toList());
    }

    public List<Product> searchAndFilter(String keyword, String category, Double minPrice, Double maxPrice, String condition, String status) {
		BigDecimal min = minPrice == null ? null : BigDecimal.valueOf(minPrice).setScale(2, RoundingMode.HALF_UP);
		BigDecimal max = maxPrice == null ? null : BigDecimal.valueOf(maxPrice).setScale(2, RoundingMode.HALF_UP);
		return searchProducts(keyword).stream()
				.filter(product -> category == null || category.isEmpty() || category.equalsIgnoreCase(product.getCategory()))
				.filter(product -> condition == null || condition.isEmpty() || condition.equalsIgnoreCase(product.getCondition()))
				.filter(product -> status == null || status.isEmpty() || status.equalsIgnoreCase(product.getStatus()))
				.filter(product -> min == null || product.getPrice().compareTo(min) >= 0)
				.filter(product -> max == null || product.getPrice().compareTo(max) <= 0)
				.collect(Collectors.toList());
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }
}

