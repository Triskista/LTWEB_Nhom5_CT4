package vn.iotstar.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Float price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = true)
    private String image;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    // Thêm constructors tùy chỉnh 
    public Product(String productName, Float price, String description, Integer stock, String image, Category category) {
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.image = image;
        this.category = category;
    }
}
