package vn.iotstar.controller.seller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;

@Controller
@RequestMapping("/seller")
public class SellerHomeController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping("")
	public String admint(HttpServletRequest request, Model model) {
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
			if (user.getRole() != null && user.getRole().getRoleName().equals("SELLER")) {
				return "seller/index"; // Trả về trang index.html
			}
		}

		return "403";

	}

	@RequestMapping("/product-add")
	public String add_product() {
		return "seller/product/add";
	}

	@RequestMapping("/product-edit")
	public String edit_product() {
		return "seller/product/edit";
	}

	@RequestMapping("/customer-add")
	public String add_customer() {
		return "seller/customer/add";
	}
	
	@GetMapping("/profile")
	public String showProfilePage(HttpServletRequest request, Model model) {
	    // Lấy email từ cookie
	    Cookie[] cookies = request.getCookies();
	    String userEmail = null;

	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if ("userEmail".equals(cookie.getName())) {
	                userEmail = cookie.getValue();
	                break;
	            }
	        }
	    }

	    if (userEmail != null) {
	        // Lấy thông tin người dùng từ email
	        Optional<User> userOptional = userService.getUserByEmail(userEmail);
	        if (userOptional.isPresent()) {
	            User user = userOptional.get();
	            model.addAttribute("user", user);
	            model.addAttribute("userEmail", userEmail); // Thêm dữ liệu vào model
	            return "seller/profile"; // Đường dẫn này phải khớp với tệp HTML
	        }
	    }

	    return "403"; // Nếu không tìm thấy thông tin, trả về lỗi
	}

	@PostMapping("/profile")
	public String updateProfile(@ModelAttribute("user") User updatedUser, HttpServletRequest request, Model model) {
	    // Lấy email từ cookie
	    Cookie[] cookies = request.getCookies();
	    String userEmail = null;

	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if ("userEmail".equals(cookie.getName())) {
	                userEmail = cookie.getValue();
	                break;
	            }
	        }
	    }

	    if (userEmail != null) {
	        // Lấy thông tin người dùng từ email
	        Optional<User> userOptional = userService.getUserByEmail(userEmail);
	        if (userOptional.isPresent()) {
	            User user = userOptional.get();

	            // Cập nhật thông tin
	            user.setUsername(updatedUser.getUsername());
	            user.setPhone(updatedUser.getPhone());

	            // Kiểm tra và mã hóa mật khẩu nếu được cập nhật
	            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
	                if (!updatedUser.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.,])[A-Za-z\\d@$!%*?&]{8,}$")) {
	                    model.addAttribute("error",
	                            "Mật khẩu không hợp lệ. Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt.");
	                    return "seller/profile";
	                }
	                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
	            }

	            // Lưu thông tin
	            userService.saveUser(user);
	            model.addAttribute("success", "Cập nhật thành công.");
	            return "seller/profile";
	        }
	    }

	    return "403"; // Nếu không tìm thấy thông tin, trả về lỗi
	}
}
