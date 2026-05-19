package fosu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Simple CLI to demonstrate product posting.
 */
public class ProductApp {
    public static void main(String[] args) {
        ProductService service = new ProductService();
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                System.out.println("\n=== Second-hand Product Posting ===");
                System.out.println("1) Post product");
                System.out.println("2) View product by id");
                System.out.println("3) List my products");
                System.out.println("4) Search by title");
                System.out.println("0) Exit");
                System.out.print("Choose: ");
                String c = scanner.nextLine().trim();
                if (c.equals("0")) break;
                switch (c) {
                    case "1":
                        System.out.print("seller username: ");
                        String seller = scanner.nextLine().trim();
                        System.out.print("title: ");
                        String title = scanner.nextLine().trim();
                        System.out.print("description: ");
                        String desc = scanner.nextLine().trim();
                        System.out.print("price (e.g. 12.34): ");
                        String priceS = scanner.nextLine().trim();
                        BigDecimal price = BigDecimal.ZERO;
                        try { price = new BigDecimal(priceS); } catch (Exception ex) { System.out.println("invalid price, set to 0"); }
                        System.out.print("category: ");
                        String cat = scanner.nextLine().trim();
                        System.out.print("condition: ");
                        String cond = scanner.nextLine().trim();
                        String id = UUID.randomUUID().toString();
                        Product p = new Product(id, title, desc, price, cat, cond, seller);
                        service.addProduct(p);
                        System.out.println("Posted: " + p);
                        break;
                    case "2":
                        System.out.print("product id: ");
                        String idq = scanner.nextLine().trim();
                        Product found = service.getProduct(idq);
                        System.out.println(found == null ? "not found" : found);
                        break;
                    case "3":
                        System.out.print("seller username: ");
                        String su = scanner.nextLine().trim();
                        List<Product> list = service.listProductsBySeller(su);
                        list.forEach(System.out::println);
                        break;
                    case "4":
                        System.out.print("keyword: ");
                        String kw = scanner.nextLine().trim();
                        List<Product> res = service.searchByTitle(kw);
                        res.forEach(System.out::println);
                        break;
                    default:
                        System.out.println("unknown");
                }
            }
        } finally {
            scanner.close();
        }
    }
}

