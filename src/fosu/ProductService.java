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
		if (sellerUsername == null) return new ArrayList<>();
		return store.values().stream().filter(p -> sellerUsername.equals(p.getSellerUsername())).collect(Collectors.toList());
	}

	public List<Product> searchByTitle(String keyword) {
		if (keyword == null || keyword.isEmpty()) return new ArrayList<>(store.values());
		String k = keyword.toLowerCase();
		return store.values().stream().filter(p -> p.getTitle() != null && p.getTitle().toLowerCase().contains(k)).collect(Collectors.toList());
	}
}

