package vn.iotstar.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.entity.Order;
import vn.iotstar.entity.OrderDetail;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	List<OrderDetail> findByOrder_OrderId(Integer orderId);
	List<OrderDetail> findByPriceBetween(Double minPrice, Double maxPrice);
	List<Order> findByDate(Date date);
}
