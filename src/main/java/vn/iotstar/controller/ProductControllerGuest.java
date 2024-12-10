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
public class ProductControllerGuest {
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@GetMapping("/")
    public String showGuestIndexPage(
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

                    return "index"; // Gọi template user/index.html
                }
            }
        }

        return "index";
    }

	@GetMapping("/{categoryName}")
	public String indexByGuest(@PathVariable("categoryName") String categoryName,
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