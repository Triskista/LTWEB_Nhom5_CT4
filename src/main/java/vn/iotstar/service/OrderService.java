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

	List<Order> findByUsernameAndDate(String username, Date date);

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
	
	 // Phân trang danh sách đơn hàng theo ngày
    Page<Order> findByDate(Date date, Pageable pageable);

    // Phân trang danh sách đơn hàng theo tên người dùng
    Page<Order> findByUserUsername(String userName, Pageable pageable);

    // Phân trang danh sách đơn hàng theo username và date
    Page<Order> findByUsernameAndDate(String username, Date date, Pageable pageable);
    
    Page<Order> findOrders(Pageable pageable);

}
