package vn.iotstar.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductService productService;

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

	/*
	 * @GetMapping("/user/index") public String indexPage(Model model) { // Lấy danh
	 * sách các danh mục List<Category> categories = categoryService.findAll();
	 * 
	 * if (categories == null || categories.isEmpty()) {
	 * System.out.println("Category list is empty or null"); } else {
	 * System.out.println("Categories size: " + categories.size()); }
	 * 
	 * // Đưa danh mục vào model model.addAttribute("categories", categories);
	 * 
	 * return "user/index"; // Tên template giao diện }
	 */

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
    @GetMapping("/user/index")
	public String index(@RequestParam(value = "search", required = false) String search, Model model) {
		List<Category> categories = categoryService.findAll();

		// Debug: Kiểm tra xem danh sách categories có dữ liệu không
		if (categories == null || categories.isEmpty()) {
			System.out.println("Category list is empty or null");
		} else {
			System.out.println("Categories size: " + categories.size());
		}

		model.addAttribute("categories", categories);
		
		List<Product> list;

		if (search != null && !search.isEmpty()) {
			// Tìm kiếm theo productName hoặc categoryName
			list = productService.searchProducts(search);
		} else {
			list = productService.findAll();
		}

		model.addAttribute("productlist", list);

		return "user/index"; // Ensure this matches your template path
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