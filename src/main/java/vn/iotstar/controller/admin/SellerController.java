package vn.iotstar.controller.admin;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;

@Controller
@RequestMapping("/admin")
public class SellerController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/seller")
	public String index(Model model) {
		List<User> list = userService.findAll();
		 List<User> list2 = new ArrayList<>();
		for (User u : list) {
			if(u.getRole() != null && u.getRole().getRoleName().equals("SELLER"))
				list2.add(u);
		}
		model.addAttribute("list", list2);
		return "admin/seller/index";
	}
}
