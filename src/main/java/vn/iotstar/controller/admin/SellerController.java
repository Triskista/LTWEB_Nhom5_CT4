package vn.iotstar.controller.admin;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/seller")
public class SellerController {
	@GetMapping
	public String index() {
		return "admin/seller/index";
	}
}
