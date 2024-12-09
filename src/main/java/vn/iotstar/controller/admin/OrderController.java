package vn.iotstar.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.iotstar.entity.Order;
import vn.iotstar.service.OrderService;


@Controller
@RequestMapping("/admin")
public class OrderController {
	@Autowired
	private OrderService orderservice;
	@GetMapping("/order")
	public String index(Model model) {
		List<Order> list = this.orderservice.findAll();
		model.addAttribute("list", list);
		return "admin/order/index";
	}
	
}
