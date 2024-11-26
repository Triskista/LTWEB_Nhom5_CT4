package vn.iotstar.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "OrderDetail")
public class OrderDetail implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderDetailId")
    private int OrderDetailId;
    

    
    @Column(name = "Quantity")
    private int Quantity;
    
    @Column(name = "Price")
    private float Price;
   
    @Column(name = "Total")
    private float Total;
    
    // Thêm mối quan hệ Many-to-One với bảng User
    @ManyToOne
    @JoinColumn(name = "OrderId", referencedColumnName = "OrderId", nullable = false)
    private Order OrderId; // Tham chiếu đối tượng User
    
    @ManyToMany
    @JoinColumn(name = "ProductId", referencedColumnName = "ProductId", nullable = false)
    private Product ProductId; // Tham chiếu đối tượng User

}
