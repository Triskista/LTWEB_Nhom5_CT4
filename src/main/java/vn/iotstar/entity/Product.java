package vn.iotstar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;
    
    @Column(name = "productName", columnDefinition = "nvarchar(255)")
	@NotEmpty(message = "khong duoc rong")
    private String productName;

    private Float price;

    @Column(name = "description", columnDefinition = "nvarchar(255)")
	@NotEmpty(message = "khong duoc rong")
    private String description;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "image", columnDefinition = "nvarchar(255)")
	@NotEmpty(message = "khong duoc rong")
    private String image;

    @ManyToOne(fetch = FetchType.LAZY) // Lấy dữ liệu khi cần (tối ưu hiệu năng)
    @JoinColumn(name = "categoryId", referencedColumnName = "categoryId", nullable = false)
    private Category category;

    // Thêm constructors tùy chỉnh 
	/*
	 * public Product(String productName, Float price, String description, Integer
	 * stock, String image, Category category) { this.productName = productName;
	 * this.price = price; this.description = description; this.stock = stock;
	 * this.image = image; this.category = category; }
	 */
	public Product(Integer productId, String productName, Float price, String description, Integer stock, String image, Category category) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.price = price;
		this.description = description;
		this.stock = stock;
		this.image = image;
		this.category = category;
	}

    
}
