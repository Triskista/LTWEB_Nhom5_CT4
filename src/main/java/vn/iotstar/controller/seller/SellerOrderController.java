package vn.iotstar.controller.seller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.iotstar.entity.Order;
import vn.iotstar.service.OrderService;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seller")
public class SellerOrderController {
    @Autowired
    private OrderService orderservice;

    @GetMapping("/order")
    public String index(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            Model model) {

        List<Order> list;

        // Kiểm tra tham số tìm kiếm
        if (username != null && !username.isEmpty() && date != null) {
            // Tìm kiếm theo cả username và date
            list = orderservice.findByUsernameAndDate(username, date);
        } else if (username != null && !username.isEmpty()) {
            // Tìm kiếm theo username
            list = orderservice.findByUserUsername(username);
        } else if (date != null) {
            // Tìm kiếm theo date
            list = orderservice.findByDate(date);
        } else {
            // Nếu không có tham số tìm kiếm nào, trả về toàn bộ danh sách
            list = orderservice.findAll();
        }

        model.addAttribute("list", list);
        return "seller/order/index";
    }
}
