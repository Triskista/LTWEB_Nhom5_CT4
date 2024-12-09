package vn.iotstar.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.iotstar.entity.OrderDetail;
import vn.iotstar.service.OrderDetailService;


@Controller

@RequestMapping("/admin")
public class OrderDetailController {
	@Autowired
	private OrderDetailService orderdetailservice;
	@GetMapping("/orderdetail")
	public String index(@RequestParam(value = "orderId", required = false) Integer orderId, Model model) {
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
	    return "admin/orderdetail/index";
	}

	
}
