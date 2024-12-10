package vn.iotstar.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.User;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.ProductService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;

@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductService productService;


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
    @GetMapping("/user/product")
    public String showProductPage() {
        return "user/product"; // Gọi file register.html trong src/main/resources/templates/
    }
    @GetMapping("/user/index")
    public String showUserIndexPage(
            HttpServletRequest request, 
            @RequestParam(value = "search", required = false) String search, 
            Model model) {
        
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
            Optional<User> optionalUser = userService.getUserByEmail(userEmail);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                model.addAttribute("userEmail", userEmail); // Thêm email vào model
                model.addAttribute("username", user.getUsername2());

                // Kiểm tra vai trò người dùng
                if (user.getRole() != null && "USER".equals(user.getRole().getRoleName())) {
                    
                    // Lấy danh sách danh mục
                    List<Category> categories = categoryService.findAll();
                    model.addAttribute("categories", categories);

                    // Lấy danh sách sản phẩm hoặc tìm kiếm nếu có từ khóa
                    List<Product> products;
                    if (search != null && !search.isEmpty()) {
                        products = productService.searchProducts(search);
                    } else {
                        products = productService.findAll();
                    }
                    model.addAttribute("productlist", products);

                    return "user/index"; // Gọi template user/index.html
                }
            }
        }

        return "403"; // Trả về trang 403 nếu không đủ điều kiện
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
    
	@GetMapping("/user/index/{categoryName}")
	public String indexByCategory(@PathVariable("categoryName") String categoryName,
			@RequestParam(value = "search", required = false) String search, Model model) {
		List<Product> products;

		if (search != null && !search.isEmpty()) {
			// Tìm kiếm theo productName hoặc categoryName
			products = productService.searchProducts(search);
		} else {
			// Lấy sản phẩm theo danh mục
			products = productService.findByCategoryName(categoryName);
		}

		model.addAttribute("list", products);
		model.addAttribute("categoryName", categoryName);

		return "user/category"; // Đường dẫn tới template
	}
}