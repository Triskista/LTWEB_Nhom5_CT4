package vn.iotstar.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.iotstar.entity.OrderDetail;
import vn.iotstar.repository.OrderDetailRepository;
import vn.iotstar.service.OrderDetailService;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

	//lấy đơn hàng chi tiết từ orderid
    @Override
    public List<OrderDetail> getOrderDetailsByOrderId(Integer orderId) {
        return orderDetailRepository.findByOrder_OrderId(orderId);
    }
    
    
    //lấy đơn hàng chi tiết từ productid
    @Override
    public List<OrderDetail> getOrderDetailsByProductId(Integer productId) {
        return orderDetailRepository.findByProduct_ProductId(productId);
    }

    //Lấy đơn hàng chi tiết từ khoảng giá trị cao hơn ...
    @Override
    public List<OrderDetail> getOrderDetailsWithQuantityGreaterThan(Integer quantity) {
        return orderDetailRepository.findByQuantityGreaterThan(quantity);
    }

    //lấy đơn hàng chi tiết từ khoản giá trị cụ thể
    @Override
    public List<OrderDetail> getOrderDetailsWithinPriceRange(Double minPrice, Double maxPrice) {
        return orderDetailRepository.findByPriceBetween(minPrice, maxPrice);
    }
}
