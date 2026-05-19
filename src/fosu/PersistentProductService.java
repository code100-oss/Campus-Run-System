package fosu;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A small wrapper service that persists products to a CSV file using savedatabase.
 * It loads products at construction (if the file exists) and provides a save method.
 */
public class PersistentProductService extends ProductService {

    private final String filePath;

    public PersistentProductService(String filePath) {
        this.filePath = filePath;
        // load existing
        try {
            List<Product> list = savedatabase.loadProductsFromCsv(filePath);
            for (Product p : list) {
                super.addProduct(p);
            }
        } catch (IOException e) {
            // ignore - start with empty store
            System.err.println("Warning: could not load products from " + filePath + ": " + e.getMessage());
        }
    }

    @Override
    public void addProduct(Product p) {
        super.addProduct(p);
        persist();
    }

    @Override
    public boolean updateProduct(String id, String title, String description, BigDecimal price, String category, String condition) {
        boolean r = super.updateProduct(id, title, description, price, category, condition);
        if (r) persist();
        return r;
    }

    @Override
    public boolean removeProduct(String id) {
        boolean r = super.removeProduct(id);
        if (r) persist();
        return r;
    }

    public void persist() {
        try {
            List<Product> all = super.listProductsBySeller(null).stream().collect(Collectors.toList());
            // listProductsBySeller(null) returns new ArrayList when null; but to get all values, use store values - ProductService doesn't expose store, so workaround: load IDs from test.
            // Instead, use searchByTitle with empty keyword to get all
            List<Product> all2 = super.searchByTitle("");
            savedatabase.saveProductsToCsv(all2, filePath);
        } catch (IOException e) {
            System.err.println("Failed to persist products: " + e.getMessage());
        }
    }
}

