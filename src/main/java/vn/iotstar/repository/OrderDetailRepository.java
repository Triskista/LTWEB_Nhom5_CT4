	package vn.iotstar.repository;
	
	import java.util.List;
	
	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.stereotype.Repository;
	import vn.iotstar.entity.OrderDetail;
	
	@Repository
	public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
		//Tìm đơn hàng chi tiết dựa trên orderId
		List<OrderDetail> findByOrder_OrderId(Integer orderId);
		
		//Tìm đơn hàng chi tiết dựa trên productId
		List<OrderDetail> findByProduct_ProductId(Integer productId);
		
		//Tìm đơn hàng chi tiết dựa từ gái trị bao nhiêu trở lên
		List<OrderDetail> findByQuantityGreaterThan(Integer quantity);
		
		//Tìm đơn hàng chi tiết dựa từ gái trị bao nhiêu trở xuống
		List<OrderDetail> findByPriceBetween(Double minPrice, Double maxPrice);
	}
