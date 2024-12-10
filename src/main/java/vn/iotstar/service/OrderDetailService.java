package vn.iotstar.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import vn.iotstar.entity.OrderDetail;

public interface OrderDetailService {
	
	
    void save(OrderDetail orderDetail);

	//lấy đơn hàng chi tiết từ orderid
    List<OrderDetail> getOrderDetailsByOrderId(Integer orderId);
    

    //lấy đơn hàng chi tiết từ productid
    List<OrderDetail> getOrderDetailsByProductId(Integer productId);
    
    //Lấy đơn hàng chi tiết từ khoảng giá trị cao hơn ...
    List<OrderDetail> getOrderDetailsWithQuantityGreaterThan(Integer quantity);
    
    //lấy đơn hàng chi tiết từ khoản giá trị cụ thể
    List<OrderDetail> getOrderDetailsWithinPriceRange(Double minPrice, Double maxPrice);


	List<OrderDetail> findAll(Sort sort);


	Page<OrderDetail> findAll(Pageable pageable);


	List<OrderDetail> findAll();


}
