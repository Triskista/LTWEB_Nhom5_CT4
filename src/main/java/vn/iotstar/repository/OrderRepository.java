package vn.iotstar.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	List<Order> findByDate(Date date);

	Page<Order> findByDateContaining(Date date, Pageable pageable);

	List<Order> findByUserUsername(String userName);
	
	List<Order> findByUserUsernameAndDate(String username, Date date);

	Page<Order> findByUserUsernameContaining(String userName, Pageable pageable);
}
