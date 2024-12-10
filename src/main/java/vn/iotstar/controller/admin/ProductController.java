package vn.iotstar.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.User;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.ProductService;
import vn.iotstar.service.UserService;

@Controller
@RequestMapping("/admin")
public class ProductController {
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private UserService userService;

	@GetMapping("/product")
	public String index(
	    @RequestParam(value = "search", required = false) String search,
	    @RequestParam(value = "page", defaultValue = "0") int page,
	    @RequestParam(value = "size", defaultValue = "5") int size,
	    Model model,
	    HttpServletRequest request) {
	    
	    Page<Product> productPage;
	    if (search != null && !search.isEmpty()) {
	        productPage = productService.searchProducts(search, page, size);
	    } else {
	        productPage = productService.findAll(page, size);
	    }

	    model.addAttribute("list", productPage.getContent()); // Danh sách sản phẩm
	    model.addAttribute("currentPage", page); // Trang hiện tại
	    model.addAttribute("totalPages", productPage.getTotalPages()); // Tổng số trang

	    // Xử lý cookie
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
	        Optional<User> user = userService.getUserByEmail(userEmail);
	        model.addAttribute("userEmail", userEmail);
	        if (user.isPresent()) {
	            model.addAttribute("username", user.get().getUsername2());
	        }
	    } else {
	        model.addAttribute("userEmail", "Không tìm thấy email");
	    }

	    return "admin/product/index";
	}



	@GetMapping("/product-add")
	public String add(HttpServletRequest request, Model model) {
		Product product = new Product(); // Khởi tạo đối tượng Product
		List<Category> categories = categoryService.findAll(); // Lấy danh sách Category
		model.addAttribute("product", product);
		model.addAttribute("categories", categories);

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
			if (user.getRole() != null && user.getRole().getRoleName().equals("ADMIN")) {
				return "admin/product/add"; // Trả về trang index.html
			}
		}

		return "403";

	}

	@PostMapping("/product-add")
	public String save(@ModelAttribute("product") Product product) {
		if (this.productService.create(product)) {
			return "redirect:/admin/product";
		} else {
			return "admin/product/add";
		}
	}

	@GetMapping("/product-edit/{productId}")
	public String edit(HttpServletRequest request, Model model, @PathVariable("productId") Integer productId) {
		Optional<Product> product = productService.findById(productId);
		List<Category> categories = categoryService.findAll();
		model.addAttribute("categories", categories);
		model.addAttribute("product", product);

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
			if (user.getRole() != null && user.getRole().getRoleName().equals("ADMIN")) {
				return "admin/product/edit"; // Trả về trang index.html
			}
		}

		return "403";
	}

	@PostMapping("/product-edit")
	public String update(@ModelAttribute("product") Product product) {
		if (this.productService.create(product)) {
			return "redirect:/admin/product";
		} else {
			return "admin/product/add";
		}
	}

	@GetMapping("/product-delete/{productId}")
	public String delete(@PathVariable("productId") Integer productId) {
		if (this.productService.delete(productId)) {
			return "redirect:/admin/product";
		} else {
			return "redirect:/admin/product";
		}
	}

}
