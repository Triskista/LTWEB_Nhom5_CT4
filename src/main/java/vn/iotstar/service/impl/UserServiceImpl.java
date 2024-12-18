package vn.iotstar.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public UserServiceImpl() {
    }
    @Override
    public User getUserById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }


    @Override
    public List<User> searchUsersByUsername(String keyword) {
        return userRepository.findByUsernameContainingIgnoreCase(keyword);
    }

    @Override
    public List<User> getAllUsersSortedByUsername() {
        return userRepository.findAllByOrderByUsernameAsc();
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public Optional<User> findById(Integer id) {
		return userRepository.findById(id);
	}

	@Override
	public void deleteUserById(Integer id) {
		userRepository.deleteById(id);
		
	}

	@Override
	public Page<User> getUsersByRole(String roleName, Pageable pageable) {
		return userRepository.findByRole_RoleName(roleName, pageable);
	}

	@Override
	public Page<User> findByRoleName(String roleName, Pageable pageable) {
		return userRepository.findByRole_RoleName(roleName, pageable);
	}

	@Override
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}
    
    /*@Override
    public User addUser(User user) {
        // Kiểm tra username hoặc email đã tồn tại hay chưa
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username đã tồn tại.");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email đã tồn tại.");
        }
        return userRepository.save(user);
    }
     */

}
