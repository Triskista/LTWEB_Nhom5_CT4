package vn.iotstar.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String index(HttpServletRequest request, Model model) {
		List<User> list = userService.findAll();
		 List<User> list2 = new ArrayList<>();
		for (User u : list) {
			if(u.getRole() != null && u.getRole().getRoleName().equals("USER"))
				list2.add(u);
		}
		model.addAttribute("list", list2);
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
		return "admin/customer/index";
	}
}
