package vn.iotstar.controller.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public String index(@RequestParam(value = "orderId", required = false) Integer orderId, 
                        @RequestParam(value = "page", defaultValue = "0") int page, 
                        @RequestParam(value = "size", defaultValue = "10") int size, 
                        HttpServletRequest request, Model model) {
        
        // Cấu hình phân trang với Pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDetailId").ascending()); // Phân trang theo "orderDetailId"
        
        Page<OrderDetail> pageResult;

        if (orderId != null) {
            // Tìm kiếm theo Order ID với phân trang
            pageResult = orderdetailservice.getOrderDetailsByOrderId(orderId, pageable);
        } else {
            // Nếu không có tìm kiếm, lấy tất cả OrderDetail với phân trang
            pageResult = orderdetailservice.findAll(pageable);
        }

        // Thêm các dữ liệu vào Model để hiển thị
        model.addAttribute("orderdetaillist", pageResult.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("totalItems", pageResult.getTotalElements());
        model.addAttribute("orderId", orderId);

        // Lấy cookie và thông tin người dùng
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
            Optional<User> u = userService.getUserByEmail(userEmail);
            model.addAttribute("userEmail", userEmail);

            if (u.isPresent()) {
                User user = u.get();
                String username2 = user.getUsername2();
                model.addAttribute("username", username2);

                // Kiểm tra quyền của người dùng
                if (user.getRole() != null && user.getRole().getRoleName().equals("ADMIN")) {
                    return "admin/orderdetail/index";
                }
            }
        }

        // Nếu không phải admin, trả về trang 403
        return "403";
    }

}
