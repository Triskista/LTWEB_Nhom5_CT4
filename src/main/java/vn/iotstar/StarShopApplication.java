package vn.iotstar;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import vn.iotstar.entity.Order;
import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.OrderService;

@SpringBootApplication
public class StarShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarShopApplication.class, args);
    }    
    
    private final UserRepository userRepository;

    // Constructor để inject UserRepository
    public StarShopApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public CommandLineRunner run(OrderService orderService) {
        return (args) -> {
            // Kiểm tra phương thức save (tạo đơn hàng mới)
            orderService.findByUserUsername("admin").forEach(order -> System.out.println("Đơn hàng của John Doe: " + order));

        };
    }
    
}
