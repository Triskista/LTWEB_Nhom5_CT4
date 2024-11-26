package vn.iotstar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Thêm phương thức tùy chỉnh nếu cần, ví dụ:
    User findByUsername(String username);
}
