package vn.iotstar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
