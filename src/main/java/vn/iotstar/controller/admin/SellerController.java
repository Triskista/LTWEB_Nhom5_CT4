package vn.iotstar.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
	public String index(Model model) {
		List<User> list = userService.findAll();
		List<User> list2 = new ArrayList<>();
		for (User u : list) {
			if (u.getRole() != null && u.getRole().getRoleName().equals("SELLER"))
				list2.add(u);
		}
		model.addAttribute("list", list2);
		return "admin/seller/index";
	}

	@GetMapping("/seller-add")
	public String showRegisterPage(Model model) {
		model.addAttribute("user", new User()); // Thêm đối tượng user vào model
		return "admin/seller/add"; // Gọi file register.html trong src/main/resources/templates/
	}

	@PostMapping("/seller-add")
	public String save(@ModelAttribute("user") User user,Model model) {

		

		// Kiểm tra trùng email
		Optional<User> existingUserEmail = userService.getUserByEmail(user.getEmail());
		if (existingUserEmail.isPresent()) {
			model.addAttribute("error", "Email đã tồn tại.");
			return "admin/seller/add"; // Trả về trang đăng ký cùng với thông báo lỗi
		}
		
		 // Kiểm tra định dạng số điện thoại
	    String phone = user.getPhone();
	    if (!phone.matches("^0\\d{9}$")) {
	        model.addAttribute("error", "Số điện thoại không hợp lệ. Số điện thoại phải có 10 chữ số và bắt đầu bằng 0.");
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
	        model.addAttribute("error", "Mật khẩu không hợp lệ. Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt.");
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

}
