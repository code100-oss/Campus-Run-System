package fosu;

import java.util.List;

public class ProductApp {
    public static void main(String[] args) {
        ProductService productService = new ProductService();

        Product item1 = new Product("seller01", "二手吉他", "九成新民谣吉他，带琴包", 450.0, "乐器", "良好");
        Product item2 = new Product("seller01", "二手手机", "iPhone 11，电池70%，屏幕完好", 1700.0, "数码", "一般");

        String id1 = productService.postProduct(item1);
        String id2 = productService.postProduct(item2);

        System.out.println("发布成功，商品ID: " + id1);
        System.out.println("发布成功，商品ID: " + id2);
        System.out.println();

        System.out.println("== 卖家 seller01 的商品列表 ==");
        List<Product> sellerProducts = productService.listProductsBySeller("seller01");
        for (Product p : sellerProducts) {
            System.out.println(p);
        }

        System.out.println();
        System.out.println("== 查看商品详情 ==");
        System.out.println(productService.getProductById(id1));
    }
}
