package vn.iotstar.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import vn.iotstar.entity.Order;

public interface OrderService {
	// Lấy danh sách đơn hàng theo ngày
    List<Order> findByDate(Date date);

    // Phân trang danh sách đơn hàng theo ngày
    Page<Order> findByDateContaining(Date date, Pageable pageable);

    // Lấy danh sách đơn hàng theo tên người dùng
    List<Order> findByUserUsername(String userName);

    // Phân trang danh sách đơn hàng theo tên người dùng
    Page<Order> findByUserUsernameContaining(String userName, Pageable pageable);

    // Lưu một đơn hàng mới hoặc cập nhật đơn hàng hiện có
    <S extends Order> S save(S order);

    // Lấy đơn hàng theo ID
    Order findById(Integer id);

    // Xóa đơn hàng theo ID
    void deleteById(Integer id);
    
    void delete(Order entity);
    
    List<Order> findAll(Sort sort);

	Page<Order> findAll(Pageable pageable);

	List<Order> findAll();
	
	long count();
}
