package vn.iotstar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
	
	User findByUsername(String username);
    User findByEmail(String email);
    User findByPhone(String phone);
    List<User> findByUsernameContainingIgnoreCase(String keyword);
    List<User> findAllByOrderByUsernameAsc();
}
