package vn.iotstar.service;

import vn.iotstar.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
	
	User getUserById(Integer id);

	User getUserByUsername(String username);

	Optional<User> getUserByEmail(String email);

	User getUserByPhone(String phone);

	//User addUser(User user);

	List<User> searchUsersByUsername(String keyword);

	List<User> getAllUsersSortedByUsername();

	User saveUser(User user);

	void deleteUser(Integer id);
}
