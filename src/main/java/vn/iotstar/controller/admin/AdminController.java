package vn.iotstar.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

	@RequestMapping("/admin")
	public String admin() {
		return "admin/index";
	}
	@RequestMapping("admin/product-add")
	public String add_product() {
		return "admin/product/add";	
	}
	@RequestMapping("admin/product-edit")
	public String edit_product() {
		return "admin/product/edit";	
	}
	@RequestMapping("admin/seller-add")
	public String add_seller() {
		return "admin/seller/add";	
	}
	@RequestMapping("admin/customer-add")
	public String add_customer() {
		return "admin/customer/add";	
	}
	@RequestMapping("admin/order-detail-view")
	public String view_order() {
		return "admin/order/view";	
	}
}
