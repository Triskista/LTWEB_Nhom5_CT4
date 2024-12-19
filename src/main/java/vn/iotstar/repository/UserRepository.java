package vn.iotstar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
	
	User findByUsername(String username);
    Optional<User> findByEmail(String email);
    User findByPhone(String phone);
    List<User> findByUsernameContainingIgnoreCase(String keyword);
    List<User> findAllByOrderByUsernameAsc();
    User findByEmailIgnoreCase(String emailId);
    
    Page<User> findByRole_RoleName(String roleName, Pageable pageable);
    Page<User> findAll(Pageable pageable);
}
