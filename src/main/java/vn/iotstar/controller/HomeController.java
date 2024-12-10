package vn.iotstar.controller;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Order;
import vn.iotstar.entity.OrderDetail;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.User;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.OrderDetailService;
import vn.iotstar.service.OrderService;
import vn.iotstar.service.ProductService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;

@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;

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
	@GetMapping("/user/index/{categoryName}/{productId}")
	public String showProductPage(@PathVariable("categoryName") String categoryName, 
	                              @PathVariable("productId") Integer productId, 
	                              Model model) {
	    // Sử dụng phương thức findByProductId để lấy thông tin sản phẩm
	    Product product = productService.findByProductId(productId);
	    if (product != null) {
	        model.addAttribute("product", product);
	        model.addAttribute("categoryName", categoryName); // Truyền thêm tên danh mục
	        return "user/product"; // Trả về trang product.html
	    } else {
	        return "error"; // Trả về trang lỗi nếu không tìm thấy sản phẩm
	    }
	}
	private Map<Integer, Integer> cart = new HashMap<>(); // Giỏ hàng tạm lưu trong bộ điều khiển

	@GetMapping("/user/cart")
	public String showCartPage(Model model) {
	    List<Product> productsInCart = new ArrayList<>();
	    List<Integer> quantitiesInCart = new ArrayList<>();
	    float grandTotal = 0;

	    for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
	        Product product = productService.findByProductId(entry.getKey());
	        if (product != null) {
	            productsInCart.add(product);
	            quantitiesInCart.add(entry.getValue());
	            grandTotal += product.getPrice() * entry.getValue();
	        }
	    }

	    model.addAttribute("cartItems", productsInCart);
	    model.addAttribute("quantities", quantitiesInCart);
	    model.addAttribute("totalAmount", grandTotal);
	    return "user/cart";  // Trả về trang giỏ hàng
	}


	@PostMapping("/user/add-to-cart")
	public String addToCart(@RequestParam("productId") Integer productId, 
	                        @RequestParam("quantity") Integer quantity, Model model) {

	    // Lấy sản phẩm theo ID
	    Product product = productService.findByProductId(productId);

	    if (product != null) {
	        // Thêm sản phẩm vào giỏ hàng (hoặc cập nhật số lượng nếu sản phẩm đã có trong giỏ)
	        cart.put(productId, cart.getOrDefault(productId, 0) + quantity);
	    }

	    // Sau khi thêm vào giỏ hàng, chuyển hướng đến trang giỏ hàng
	    return "redirect:/user/cart";
	}
	@PostMapping("/user/update-cart")
	public String updateCart(@RequestParam("productId") Integer productId, 
	                         @RequestParam("quantity") Integer quantity,
	                         Model model) {

	    if (quantity <= 0) {
	        cart.remove(productId); // Nếu số lượng là 0 hoặc âm, xóa sản phẩm khỏi giỏ hàng
	    } else {
	        cart.put(productId, quantity); // Cập nhật số lượng mới cho sản phẩm
	    }

	    return "redirect:/user/cart"; // Quay lại trang giỏ hàng
	}

	@PostMapping("/user/place-order")
	public String placeOrder(HttpServletRequest request, Model model) {
	    try {
	        // Lấy userId từ cookie
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

	        if (userEmail == null) {
	            model.addAttribute("error", "Bạn cần đăng nhập để đặt hàng.");
	            return "user/cart"; // Quay lại trang giỏ hàng nếu chưa đăng nhập
	        }

	        Optional<User> optionalUser = userService.getUserByEmail(userEmail);
	        if (!optionalUser.isPresent()) {
	            model.addAttribute("error", "Không tìm thấy tài khoản người dùng.");
	            return "user/cart";
	        }

	        User user = optionalUser.get();

	        // Tạo một đơn hàng mới
	        Order order = new Order();
	        order.setUser(user);
	        order.setDate(new Date());
	        order.setTotalPrice(0.0f); // Sẽ tính sau
	        orderService.save(order);

	        // Lưu chi tiết đơn hàng và cập nhật kho sản phẩm
	        float totalPrice = 0;
	        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
	            Integer productId = entry.getKey();
	            Integer quantity = entry.getValue();

	            Product product = productService.findByProductId(productId);
	            if (product == null || product.getStock() < quantity) {
	                model.addAttribute("error", "Không đủ hàng trong kho cho sản phẩm: " + product.getProductName());
	                return "user/cart"; // Quay lại giỏ hàng nếu không đủ hàng
	            }

	            // Giảm số lượng tồn kho
	            product.setStock(product.getStock() - quantity);
	            productService.save(product);

	            // Lưu chi tiết đơn hàng
	            OrderDetail orderDetail = new OrderDetail();
	            orderDetail.setOrder(order);
	            orderDetail.setProduct(product);
	            orderDetail.setQuantity(quantity);
	            orderDetail.setPrice(product.getPrice());
	            orderDetail.setTotal(product.getPrice() * quantity);
	            orderDetailService.save(orderDetail);

	            totalPrice += orderDetail.getTotal();
	        }

	        // Cập nhật tổng giá trị đơn hàng
	        order.setTotalPrice(totalPrice);
	        orderService.save(order);

	        // Xóa giỏ hàng sau khi đặt hàng thành công
	        cart.clear();

	        // Thông báo thành công và chuyển hướng về trang sản phẩm
	        model.addAttribute("message", "Đặt hàng thành công!");
	        return "redirect:/user/index"; // Chuyển hướng về trang sản phẩm
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("error", "Đã xảy ra lỗi khi đặt hàng.");
	        return "user/cart";
	    }
	}



}