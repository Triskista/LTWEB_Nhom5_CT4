package vn.iotstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.iotstar.entity.Product;
import vn.iotstar.service.ProductService;

@Controller
public class ProductControllerUser {

	@Autowired
    private ProductService productService;

    // Đường dẫn tới trang hiển thị danh sách sản phẩm
    @GetMapping("/products")
    public String showAllProducts(Model model) {
        // Lấy danh sách sản phẩm từ service
        model.addAttribute("products", productService.findAll());
        return "index";  // Tên view sẽ là "products.html"
    }
}
