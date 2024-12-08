package vn.iotstar.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.iotstar.entity.OrderDetail;
import vn.iotstar.service.OrderDetailService;


@Controller

@RequestMapping("/admin")
public class OrderDetailController {
	@Autowired
	private OrderDetailService orderdetailservice;
	@GetMapping("/orderdetail")
	public String index(Model model) {
		List<OrderDetail> list = this.orderdetailservice.findAll();
		model.addAttribute("orderdetaillist", list);
		return "admin/orderdetail/index";
	}
	
}
