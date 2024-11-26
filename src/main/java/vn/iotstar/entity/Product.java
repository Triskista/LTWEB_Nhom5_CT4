package vn.iotstar.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Khóa chính tự động tăng
    @Column(name = "ProductId")
    private int productId;

    @Column(name = "ProductName", nullable = false, length = 100) // Tên sản phẩm
    private String productName;

    @Column(name = "Price", nullable = false) // Giá sản phẩm
    private float price;

    @Column(name = "Description", columnDefinition = "NVARCHAR(MAX)") // Mô tả sản phẩm
    private String description;

    @Column(name = "Stock", nullable = false) // Số lượng tồn kho
    private int stock;

    @Column(name = "Image", columnDefinition = "NVARCHAR(MAX)") // Link ảnh
    private String image;

    @ManyToOne(fetch = FetchType.LAZY) // Quan hệ nhiều sản phẩm thuộc một danh mục
    @JoinColumn(name = "CategoryId", referencedColumnName = "CategoryId", nullable = false) // Khóa ngoại đến Category
    private Category category;

    // Constructor không tham số (bắt buộc bởi JPA)
    public Product() {
    }

    // Constructor đầy đủ
    public Product(String productName, float price, String description, int stock, String image, Category category) {
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.image = image;
        this.category = category;
    }

    // Getter và Setter
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", stock=" + stock +
                ", image='" + image + '\'' +
                ", category=" + (category != null ? category.getCategoryName() : "null") +
                '}';
    }
}