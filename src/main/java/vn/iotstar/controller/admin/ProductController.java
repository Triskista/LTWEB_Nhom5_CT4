package vn.iotstar.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.iotstar.entity.Product;
import vn.iotstar.service.ProductService;

@Controller
@RequestMapping("/admin")
public class ProductController {
	@Autowired
	private ProductService productService;
	@GetMapping("/product")
	public String index(Model model) {
		List<Product> list = this.productService.findAll();
		model.addAttribute("list", list);
		return "admin/product/index";
	}
	/*
	 * @RequestMapping("/product-add") private String add() { return
	 * "admin/product/add"; }
	 */
	
}
