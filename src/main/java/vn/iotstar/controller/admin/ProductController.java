package vn.iotstar.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.ProductService;

@Controller
@RequestMapping("/admin")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/product")
    public String index(Model model) {
        List<Product> list = this.productService.findAll();
        model.addAttribute("list", list);
        return "admin/product/index";
    }

    @GetMapping("/product-add")
    public String add(Model model) {
        Product product = new Product(); // Khởi tạo đối tượng Product
        List<Category> categories = categoryService.findAll(); // Lấy danh sách Category
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "admin/product/add";
    }

    @PostMapping("/product-add")
    public String save(@ModelAttribute("product") Product product) {
        if (this.productService.create(product)) {
            return "redirect:/admin/product"; 
        } else {
            return "admin/product/add";
        }
    }
    
    @GetMapping("/product-edit/{productId}")
    public String edit(Model model, @PathVariable("productId") Integer productId) {
        Optional<Product> product = productService.findById(productId);
        List<Category> categories = categoryService.findAll(); 
        model.addAttribute("categories", categories);
        model.addAttribute("product", product);
        return "admin/product/edit"; 
    }
    
    @PostMapping("/product-edit")
    public String update(@ModelAttribute("product") Product product) {
        if (this.productService.create(product)) {
            return "redirect:/admin/product"; 
        } else {
            return "admin/product/add";
        }
    }
    
    @GetMapping("/product-delete/{productId}")
    public String delete(@PathVariable("productId") Integer productId) {
        if (this.productService.delete(productId)) {
            return "redirect:/admin/product"; 
        } else {
            return "redirect:/admin/product";
        }
    }

}
