package vn.iotstar.controller.seller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;

@Controller
@RequestMapping("/seller")
public class SellerHomeController {

	@Autowired
	private UserService userService;

	@RequestMapping("")
	public String admint() {
		return "seller/index";
	}

	@RequestMapping("/product-add")
	public String add_product() {
		return "seller/product/add";
	}

	@RequestMapping("/product-edit")
	public String edit_product() {
		return "seller/product/edit";
	}


	@RequestMapping("/customer-add")
	public String add_customer() {
		return "seller/customer/add";
	}

}
