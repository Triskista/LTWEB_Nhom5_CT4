package vn.iotstar.controller.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;

@Controller
@RequestMapping("/admin")
public class CustomerController {
	@Autowired
	private UserService userService;

	@GetMapping("/customer")
    public String index(HttpServletRequest request, Model model,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        
        // Cấu hình Pageable cho phân trang
        Pageable pageable = PageRequest.of(page, size);
        
        // Lấy danh sách người dùng có role là "USER"
        Page<User> userPage = userService.findByRoleName("USER", pageable);

        // Chuyển dữ liệu vào model
        model.addAttribute("list", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("size", size);

        // Lấy thông tin người dùng từ cookie
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
            if (user.getRole() != null && user.getRole().getRoleName().equals("ADMIN")) {
                return "admin/customer/index"; // Trả về trang index.html
            }
        }

        return "403"; // Nếu không phải Admin
    }
}
