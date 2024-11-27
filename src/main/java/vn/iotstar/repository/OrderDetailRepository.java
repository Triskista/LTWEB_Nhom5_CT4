package vn.iotstar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
	List<OrderDetail> findByOrder_OrderId(Integer orderId);
	List<OrderDetail> findByProduct_ProductId(Integer productId);
	List<OrderDetail> findByQuantityGreaterThan(Integer quantity);
	List<OrderDetail> findByPriceBetween(Double minPrice, Double maxPrice);
}
