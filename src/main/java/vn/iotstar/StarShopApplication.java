package vn.iotstar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

@SpringBootApplication
public class StarShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarShopApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            RoleRepository roleRepository
    ) {
        return args -> {
            // 1. Tạo vai trò
            Role adminRole = new Role("ADMIN");
            Role userRole = new Role("USER");
            roleRepository.save(adminRole);
            roleRepository.save(userRole);

            // 2. Tạo người dùng
            User admin = new User("admin", "admin123", "admin@example.com", "0123456789", adminRole, true);
            User user = new User("user", "user123", "user@example.com", "0987654321", userRole, true);
            userRepository.save(admin);
            userRepository.save(user);

            // 3. Tạo danh mục sản phẩm
            Category flowers = new Category("Flowers");
            Category plants = new Category("Plants");
            categoryRepository.save(flowers);
            categoryRepository.save(plants);

         // 4. Tạo sản phẩm
            Product rose = new Product("Rose Bouquet", 50.0f, "A beautiful bouquet of roses", 100, "rose.jpg", flowers);
            Product bonsai = new Product("Bonsai Tree", 150.0f, "A small and elegant bonsai tree", 10, "bonsai.jpg", plants);
            productRepository.save(rose);
            productRepository.save(bonsai);

            System.out.println("Database initialized successfully.");
        };
    }
}
