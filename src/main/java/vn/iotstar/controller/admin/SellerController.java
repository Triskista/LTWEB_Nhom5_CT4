package vn.iotstar.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.models.RegisterUserModel;
import vn.iotstar.service.UserService;

@Controller
@RequestMapping("/admin")
public class SellerController {
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/seller")
	public String index(@RequestParam(value = "page", defaultValue = "0") int page, 
	                    @RequestParam(value = "size", defaultValue = "5") int size, 
	                    HttpServletRequest request, Model model) {
	    // Cấu hình phân trang với Pageable
	    Pageable pageable = PageRequest.of(page, size, Sort.by("userId").ascending()); // phân trang theo userId

	    Page<User> pageResult = userService.getUsersByRole("SELLER", pageable); // Lấy các SELLER với phân trang

	    // Lọc danh sách SELLER
	    model.addAttribute("list", pageResult.getContent()); // Lấy nội dung của trang hiện tại
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", pageResult.getTotalPages());
	    model.addAttribute("totalItems", pageResult.getTotalElements());

	    // Lấy tất cả các cookie từ request
	    Cookie[] cookies = request.getCookies();
	    String userEmail = null;

	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if ("userEmail".equals(cookie.getName())) {
	                userEmail = cookie.getValue(); // Lấy giá trị của cookie userEmail
	                break;
	            }
	        }
	    }
	    if (userEmail != null) {
	        Optional<User> u = userService.getUserByEmail(userEmail);
	        model.addAttribute("userEmail", userEmail); // Thêm dữ liệu vào model

	        User user = u.get();
	        String username2 = user.getUsername2();
	        model.addAttribute("username", username2);
	        if (user.getRole() != null && user.getRole().getRoleName().equals("ADMIN")) {
	            return "admin/seller/index"; // Trả về trang index.html
	        }
	    }

	    return "403";
	}

	@GetMapping("/seller-add")
	public String showRegisterPage(HttpServletRequest request, Model model) {
		// Lấy tất cả các cookie từ request
		Cookie[] cookies = request.getCookies();
		String userEmail = null;

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("userEmail".equals(cookie.getName())) {
					userEmail = cookie.getValue(); // Lấy giá trị của cookie userEmail
					break;
				}
			}
		}
		if (userEmail != null) {
			Optional<User> u = userService.getUserByEmail(userEmail);
			model.addAttribute("userEmail", userEmail); // Thêm dữ liệu vào model

			User user = u.get();
			String username2 = user.getUsername2();
			model.addAttribute("username", username2);
			if (user.getRole() != null && user.getRole().getRoleName().equals("ADMIN")) {
				model.addAttribute("user", new User());
				return "admin/seller/add"; // Trả về trang index.html
			}
		}

		return "403";

	}

	@PostMapping("/seller-add")
	public String save(@ModelAttribute("user") User user, Model model) {

		// Kiểm tra trùng email
		Optional<User> existingUserEmail = userService.getUserByEmail(user.getEmail());
		if (existingUserEmail.isPresent()) {
			model.addAttribute("error", "Email đã tồn tại.");
			return "admin/seller/add"; // Trả về trang đăng ký cùng với thông báo lỗi
		}

		// Kiểm tra định dạng số điện thoại
		String phone = user.getPhone();
		if (!phone.matches("^0\\d{9}$")) {
			model.addAttribute("error",
					"Số điện thoại không hợp lệ. Số điện thoại phải có 10 chữ số và bắt đầu bằng 0.");
			return "admin/seller/add"; // Trả về trang thêm với thông báo lỗi
		}

		// Kiểm tra trùng phone
		User existingUserPhone = userService.getUserByPhone(user.getPhone());
		if (existingUserPhone != null) {
			model.addAttribute("error", "Số điện thoại đã tồn tại.");
			return "admin/seller/add"; // Trả về trang đăng ký cùng với thông báo lỗi
		}
		// Kiểm tra mật khẩu
		String password = user.getPassword();
		if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.,])[A-Za-z\\d@$!%*?&]{8,}$")) {
			model.addAttribute("error",
					"Mật khẩu không hợp lệ. Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt.");
			return "admin/seller/add"; // Trả về trang thêm với thông báo lỗi
		}
		// Tạo đối tượng Role
		Role role = new Role(3, "SELLER");
		user.setRole(role);
		user.setStatus(true); // status là true
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		// Lưu user vào cơ sở dữ liệu
		if (userService.saveUser(user) != null) {
			// Nếu lưu thành công, chuyển hướng tới danh sách seller
			return "redirect:/admin/seller";
		} else {
			// Nếu lưu không thành công, quay lại trang thêm
			model.addAttribute("error", "Lỗi khi lưu người dùng.");
			return "admin/seller/add"; // Trả về trang đăng ký cùng với thông báo lỗi
		}
	}

	@GetMapping("/seller-edit")
	public String showEditPage(@RequestParam("id") Integer id, HttpServletRequest request, Model model) {
		Optional<User> userOptional = userService.findById(id);

		// Lấy tất cả các cookie từ request
		Cookie[] cookies = request.getCookies();
		String userEmail = null;

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("userEmail".equals(cookie.getName())) {
					userEmail = cookie.getValue(); // Lấy giá trị của cookie userEmail
					break;
				}
			}
		}
		if (userEmail != null) {
			Optional<User> u = userService.getUserByEmail(userEmail);
			model.addAttribute("userEmail", userEmail); // Thêm dữ liệu vào model

			User user = u.get();
			String username2 = user.getUsername2();
			model.addAttribute("username", username2);
			model.addAttribute("user", userOptional.get());
			if (user.getRole() != null && user.getRole().getRoleName().equals("ADMIN")) {
				return "admin/seller/edit"; // Trả về trang index.html
			}
		}

		return "403";

		
	}

	@PostMapping("/seller-edit")
	public String updateUser(@ModelAttribute("user") User user, @RequestParam("oldPassword") String oldPassword,
			Model model) {
		// Tìm người dùng hiện tại từ cơ sở dữ liệu
		Optional<User> existingUser = userService.findById(user.getUserId());
		if (existingUser.isPresent()) {
			User currentUser = existingUser.get();

			// Kiểm tra mật khẩu cũ
			if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
				model.addAttribute("error", "Mật khẩu cũ không chính xác.");
				return "admin/seller/edit"; // Trả về trang edit với thông báo lỗi
			}

			// Kiểm tra email trùng (trừ chính user đang sửa)
			Optional<User> existingUserEmail = userService.getUserByEmail(user.getEmail());
			if (existingUserEmail.isPresent() && !existingUserEmail.get().getUserId().equals(user.getUserId())) {
				model.addAttribute("error", "Email đã tồn tại.");
				return "admin/seller/edit";
			}

			// Kiểm tra định dạng số điện thoại
			String phone = user.getPhone();
			if (!phone.matches("^0\\d{9}$")) {
				model.addAttribute("error",
						"Số điện thoại không hợp lệ. Số điện thoại phải có 10 chữ số và bắt đầu bằng 0.");
				return "admin/seller/edit";
			}

			// Kiểm tra mật khẩu mới (nếu người dùng nhập mật khẩu mới)
			String newPassword = user.getPassword();
			if (!newPassword.isEmpty()) {
				// Kiểm tra độ mạnh của mật khẩu
				if (!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.,])[A-Za-z\\d@$!%*?&]{8,}$")) {
					model.addAttribute("error",
							"Mật khẩu không hợp lệ. Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt.");
					return "admin/seller/edit";
				}

				// Mã hóa mật khẩu mới nếu hợp lệ và khác với mật khẩu cũ
				if (!passwordEncoder.matches(newPassword, currentUser.getPassword())) {
					currentUser.setPassword(passwordEncoder.encode(newPassword));
				} else {
					model.addAttribute("error", "Mật khẩu mới không được trùng với mật khẩu cũ.");
					return "admin/seller/edit";
				}
			}

			// Cập nhật thông tin cơ bản
			currentUser.setUsername(user.getUsername());
			currentUser.setEmail(user.getEmail());
			currentUser.setPhone(user.getPhone());
			userService.saveUser(currentUser);

			return "redirect:/admin/seller";
		} else {
			model.addAttribute("error", "Không tìm thấy người dùng.");
			return "admin/seller/edit";
		}
	}

	@GetMapping("/seller-delete")
	public String deleteUser(@RequestParam("id") Integer id, Model model) {
		Optional<User> userOptional = userService.findById(id);
		if (userOptional.isPresent()) {
			userService.deleteUserById(id);
		} else {
			model.addAttribute("error", "Nhân viên không tồn tại.");
		}
		return "redirect:/admin/seller";
	}

}
