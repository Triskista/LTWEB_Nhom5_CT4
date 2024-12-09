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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
            String username = user.getUsername2();
            model.addAttribute("username", username);
        } else {
            model.addAttribute("userEmail", "Không tìm thấy email"); // Nếu không có cookie
        }
		
		
		return "seller/index";
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

}
