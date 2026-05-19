package fosu;

import java.math.BigDecimal;

public class Product {
    private String id;
    private String title;
    private String description;
    private BigDecimal price;
    private String category;
    private String condition;
    private String seller;

    public Product() {}

    public Product(String id, String title, String description, BigDecimal price, String category, String condition, String seller) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.condition = condition;
        this.seller = seller;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getSeller() { return seller; }
    public void setSeller(String seller) { this.seller = seller; }

    @Override
    public String toString() {
        return String.format("Product{id='%s', title='%s', price=%s, category='%s', condition='%s', seller='%s'}",
                id, title, price, category, condition, seller);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id != null ? id.equals(product.id) : product.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

