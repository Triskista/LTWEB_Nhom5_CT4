package vn.iotstar.controller.seller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import vn.iotstar.entity.Order;
import vn.iotstar.entity.User;
import vn.iotstar.service.OrderService;
import vn.iotstar.service.UserService;

import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/seller")
public class SellerOrderController {
	@Autowired
	private OrderService orderservice;
	@Autowired
	private UserService userService;

	@GetMapping("/order")
	public String index(
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			HttpServletRequest request, 
			Model model) {

		Pageable pageable = PageRequest.of(page, size);
		Page<Order> orderPage;

		// Kiểm tra tham số tìm kiếm
		if (username != null && !username.isEmpty() && date != null) {
			// Tìm kiếm theo cả username và date
			orderPage = orderservice.findByUsernameAndDate(username, date, pageable);
		} else if (username != null && !username.isEmpty()) {
			// Tìm kiếm theo username
			orderPage = orderservice.findByUserUsername(username, pageable);
		} else if (date != null) {
			// Tìm kiếm theo date
			orderPage = orderservice.findByDate(date, pageable);
		} else {
			// Nếu không có tham số tìm kiếm nào, trả về toàn bộ danh sách
			orderPage = orderservice.findAll(pageable);
		}

		model.addAttribute("list", orderPage.getContent());
		model.addAttribute("currentPage", orderPage.getNumber());
		model.addAttribute("totalPages", orderPage.getTotalPages());
		model.addAttribute("username", username);
		model.addAttribute("date", date);

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
			if (user.getRole() != null && user.getRole().getRoleName().equals("SELLER")) {
				return "seller/order/index"; // Trả về trang index.html
			}
		}

		return "403";
	}
}
