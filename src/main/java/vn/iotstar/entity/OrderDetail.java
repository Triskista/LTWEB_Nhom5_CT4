package vn.iotstar.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OrderDetail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderDetailId;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private float price;

    @Column(nullable = false)
    private float total;
}
