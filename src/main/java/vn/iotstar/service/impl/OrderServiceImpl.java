package vn.iotstar.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.iotstar.entity.Order;
import vn.iotstar.repository.OrderRepository;
import vn.iotstar.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderRepository orderRepository;

	@Override
	public List<Order> findByDate(Date date) {
		// Trả về danh sách đơn hàng theo ngày
		return orderRepository.findByDate(date);
	}

	@Override
	public Page<Order> findByDateContaining(Date date, Pageable pageable) {
		// Trả về danh sách đơn hàng theo ngày, phân trang
		return orderRepository.findByDateContaining(date, pageable);
	}

	@Override
	public <S extends Order> S save(S order) {
		// Kiểm tra nếu đơn hàng không có ID thì tạo mới, ngược lại thì cập nhật
		return orderRepository.save(order);
	}

	@Override
	public Order findById(Integer id) {
		// Tìm đơn hàng theo ID, trả về Optional
		Optional<Order> order = orderRepository.findById(id);
		return order.orElse(null); // Nếu không tìm thấy, trả về null
	}

	@Override
	public void deleteById(Integer id) {
		// Xóa đơn hàng theo ID
		orderRepository.deleteById(id);
	}

	@Override
	public void delete(Order entity) {
		// Xóa đơn hàng theo đối tượng entity
		orderRepository.delete(entity);
	}

	@Override
	public List<Order> findAll(Sort sort) {
		// Trả về danh sách tất cả đơn hàng, sắp xếp theo yêu cầu
		return orderRepository.findAll(sort);
	}

	@Override
	public Page<Order> findAll(Pageable pageable) {
		// Trả về danh sách tất cả đơn hàng, phân trang
		return orderRepository.findAll(pageable);
	}

	@Override
	public List<Order> findAll() {
		// Trả về danh sách tất cả đơn hàng
		return orderRepository.findAll();
	}

	@Override
	public long count() {
		// Trả về số lượng đơn hàng
		return orderRepository.count();
	}

	@Override
	public List<Order> findByUserUsername(String userName) {
		// Trả về danh sách đơn hàng theo tên người dùng
		return orderRepository.findByUserUsername(userName);
	}

	@Override
	public Page<Order> findByUserUsernameContaining(String userName, Pageable pageable) {
		// Trả về danh sách đơn hàng theo tên người dùng, phân trang
		return orderRepository.findByUserUsernameContaining(userName, pageable);
	}
	@Override
	public List<Order> findByUsernameAndDate(String username, Date date) {
	    return orderRepository.findByUserUsernameAndDate(username, date);
	}

	@Override
	public Page<Order> findByDate(Date date, Pageable pageable) {
		return orderRepository.findByDateContaining(date, pageable);
	}

	@Override
	public Page<Order> findByUserUsername(String userName, Pageable pageable) {
		// Trả về danh sách đơn hàng theo tên người dùng, phân trang
        return orderRepository.findByUserUsernameContaining(userName, pageable);
	}

	@Override
	public Page<Order> findByUsernameAndDate(String username, Date date, Pageable pageable) {
	    // Sử dụng phương thức phân trang từ repository
	    return orderRepository.findByUserUsernameAndDate(username, date, pageable);
	}

	@Override
	public Page<Order> findOrders(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}

}
