package fosu;

import java.util.List;

public class ProductSearchApp {
    public static void main(String[] args) {
        ProductService productService = new ProductService();

        productService.postProduct(new Product("seller01", "二手平板电脑", "iPad Air 3，九成新，带保护套", 1200.0, "数码", "很新"));
        productService.postProduct(new Product("seller02", "二手吉他", "学过半年，保养良好，带琴包", 550.0, "乐器", "良好"));
        productService.postProduct(new Product("seller03", "二手教科书", "大学教材，含习题答案，适合大一学生", 80.0, "书籍", "一般"));
        productService.postProduct(new Product("seller04", "二手羽毛球拍", "标准拍，拍线完好", 90.0, "运动", "很新"));

        System.out.println("== 搜索关键字 '二手' ==");
        List<Product> searchResults = productService.searchProducts("二手");
        searchResults.forEach(System.out::println);

        System.out.println("\n== 筛选分类: 数码, 价格区间: 500-1500 ==");
        List<Product> filterResults = productService.filterProducts("数码", 500.0, 1500.0, null, "available");
        filterResults.forEach(System.out::println);

        System.out.println("\n== 搜索+筛选: 关键字 '教材', 分类 '书籍' ==");
        List<Product> combinedResults = productService.searchAndFilter("教材", "书籍", null, null, null, "available");
        combinedResults.forEach(System.out::println);
    }
}
