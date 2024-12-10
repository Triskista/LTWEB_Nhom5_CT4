package vn.iotstar.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;

@Controller
public class HomeController {
	@Autowired
	private UserService userService;
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Gọi file login.html trong src/main/resources/templates/
    }

    
    @GetMapping("/user/profile")
    public String authenticatedUser(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            model.addAttribute("user", currentUser);
            return "profile";  // Trả về trang profile.html
        } catch (Exception e) {
            e.printStackTrace();  // In lỗi ra console để dễ dàng debug
            return "error";  // Có thể trả về trang lỗi nếu có lỗi xảy ra
        }
    }

    

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // Gọi file register.html trong src/main/resources/templates/
    }

    @GetMapping("/user/index")
    public String showUserIndexPage(HttpServletRequest request, Model model) {
    	
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
    				if (user.getRole() != null && user.getRole().getRoleName().equals("USER")) {
    					return "user/index"; // Gọi file index.html trong src/main/resources/templates/user/
    				}
    			}

    			return "403";
    	
        
    }
    
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(403).body("No user is logged in.");
        }
        
        // Lấy tên người dùng
        String username = authentication.getName();
        
        // Lấy danh sách các role (authorities)
        Object roles = authentication.getAuthorities();

        return ResponseEntity.ok().body("Username: " + username + ", Roles: " + roles);
    }

}
