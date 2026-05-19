package fosu;

import java.math.BigDecimal;
import java.util.List;

public class ProductTest {
    public static void main(String[] args) {
        ProductService service = new ProductService();

        Product a = new Product("p1", "Old Laptop", "A used laptop, works fine", new BigDecimal("250.00"), "Electronics", "Used", "bob");
        Product b = new Product("p2", "Math Textbook", "Calculus textbook", new BigDecimal("20.00"), "Books", "Like New", "alice");
        service.addProduct(a);
        service.addProduct(b);

        System.out.println("Get p1: " + service.getProduct("p1"));
        System.out.println("Search 'lap':");
        List<Product> s = service.searchByTitle("lap");
        s.forEach(System.out::println);

        System.out.println("List by seller alice:");
        service.listProductsBySeller("alice").forEach(System.out::println);
    }
}

