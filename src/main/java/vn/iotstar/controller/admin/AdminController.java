package vn.iotstar.controller.admin;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;

@Controller
@RequestMapping("/")
public class AdminController {

	@Autowired
	private UserService userService;

	@GetMapping("admin2")
	public RedirectView adminAccess(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		String email = "";
		for (Cookie cookie : cookies) {
			// Kiểm tra nếu cookie có tên là "username"
			if ("userEmail".equals(cookie.getName())) {
				// Trả về giá trị của cookie "username"
				email = cookie.getValue();
			}
		}
		if (email == null || email.isEmpty()) {
			return new RedirectView("/error"); // Handle missing email
		}

		Optional<User> optionalUser = userService.getUserByEmail(email); // Use your existing method

		return optionalUser.map(user -> handleUserAccess(user, request)) // Handle the user if found
				.orElse(new RedirectView("/accessDenied")); // Redirect to access denied if not found
	}

	private RedirectView handleUserAccess(User user, HttpServletRequest request) {
		// Check if user is admin or not then redirect to another page

		if (user.getRole() != null && user.getRole().getRoleName().equals("ADMIN")) {
			return new RedirectView(request.getContextPath() + "admin");
		} else if (user.getRole() != null && user.getRole().getRoleName().equals("USER"))
			return new RedirectView(request.getContextPath() + "user/index");
		else
			return new RedirectView(request.getContextPath() + "seller");
	}

	@RequestMapping("admin")
	public String adminDashboard(HttpServletRequest request, Model model) {
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
            String username = user.getUsername2();
            model.addAttribute("username", username);
        } else {
            model.addAttribute("userEmail", "Không tìm thấy email"); // Nếu không có cookie
        }

        return "admin/index"; // Trả về trang index.html
	}

	@RequestMapping("user/product-add")
	public String add_product() {
		return "admin/product/add";
	}

	@RequestMapping("admin/product-edit")
	public String edit_product() {
		return "admin/product/edit";
	}

	@RequestMapping("admin/seller-add")
	public String add_seller() {
		return "admin/seller/add";
	}

	@RequestMapping("admin/customer-add")
	public String add_customer() {
		return "admin/customer/add";
	}

}
