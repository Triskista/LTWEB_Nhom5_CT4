package vn.iotstar.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import vn.iotstar.entity.OrderDetail;
import vn.iotstar.entity.User;
import vn.iotstar.service.OrderDetailService;
import vn.iotstar.service.UserService;

@Controller

@RequestMapping("/admin")
public class OrderDetailController {
	@Autowired
	private OrderDetailService orderdetailservice;
	@Autowired
	private UserService userService;

	@GetMapping("/orderdetail")
	public String index(@RequestParam(value = "orderId", required = false) Integer orderId, HttpServletRequest request,
			Model model) {
		List<OrderDetail> list;
		if (orderId != null) {
			// Search by Order ID
			list = orderdetailservice.getOrderDetailsByOrderId(orderId);
		} else {
			// If no search term is provided, return all records
			list = orderdetailservice.findAll();
		}
		model.addAttribute("orderdetaillist", list);
		model.addAttribute("orderId", orderId);

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
				return "admin/orderdetail/index"; // Trả về trang index.html
			}
		}

		return "403";
	}

}
