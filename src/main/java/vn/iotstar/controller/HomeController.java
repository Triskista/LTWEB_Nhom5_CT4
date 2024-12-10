package vn.iotstar.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import vn.iotstar.entity.ConfirmationToken;
import vn.iotstar.entity.User;
import vn.iotstar.repository.ConfirmationTokenRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.EmailSenderService;
import vn.iotstar.service.UserService;

@Controller
public class HomeController {
	@Autowired
	private UserService userService;
	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;
	@Autowired
	private EmailSenderService emailSenderService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Gọi file login.html trong src/main/resources/templates/
    }

    
    @GetMapping("/user/profile")
    public String authenticatedUser(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            model.addAttribute("user", currentUser);
            return "profile";  // Trả về trang profile.html
        } catch (Exception e) {
            e.printStackTrace();  // In lỗi ra console để dễ dàng debug
            return "error";  // Có thể trả về trang lỗi nếu có lỗi xảy ra
        }
    }

    

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // Gọi file register.html trong src/main/resources/templates/
    }

    @GetMapping("/user/index")
    public String showUserIndexPage(HttpServletRequest request, Model model) {
    	
    	// Lấy tất cả các cookie từ request
    			Cookie[] cookies = request.getCookies();
    			String userEmail = null;

    			if (cookies != null) {
    				for (Cookie cookie : cookies) {
    					if ("userEmail".equals(cookie.getName())) {
    						userEmail = cookie.getValue(); // Lấy giá trị của cookie userEmail
    						break;
    					}
    				}
    			}
    			if (userEmail != null) {
    				Optional<User> u = userService.getUserByEmail(userEmail);
    				model.addAttribute("userEmail", userEmail); // Thêm dữ liệu vào model

    				User user = u.get();
    				String username2 = user.getUsername2();
    				model.addAttribute("username", username2);
    				if (user.getRole() != null && user.getRole().getRoleName().equals("USER")) {
    					return "user/index"; // Gọi file index.html trong src/main/resources/templates/user/
    				}
    			}

    			return "403";
    	
        
    }
    
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(403).body("No user is logged in.");
        }
        
        // Lấy tên người dùng
        String username = authentication.getName();
        
        // Lấy danh sách các role (authorities)
        Object roles = authentication.getAuthorities();

        return ResponseEntity.ok().body("Username: " + username + ", Roles: " + roles);
    }
    
    
    
    // Display the form
    @RequestMapping(value="/forgot-password", method=RequestMethod.GET)
    public ModelAndView displayResetPassword(ModelAndView modelAndView, User user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName("forgotPassword");
        return modelAndView;
    }

    // Receive the address and send an email
    @RequestMapping(value="/forgot-password", method=RequestMethod.POST)
    public ModelAndView forgotUserPassword(ModelAndView modelAndView, User user) {
        User existingUser = userRepository.findByEmailIgnoreCase(user.getEmail());
        if (existingUser != null) {
            // Create token
            ConfirmationToken confirmationToken = new ConfirmationToken(existingUser);

            // Save it
            confirmationTokenRepository.save(confirmationToken);

            // Create the email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(existingUser.getEmail());
            mailMessage.setSubject("Complete Password Reset!");
            mailMessage.setFrom("test-email@gmail.com");
            mailMessage.setText("To complete the password reset process, please click here: "
              + "http://localhost:8090/confirm-reset?token="+confirmationToken.getConfirmationToken());

            // Send the email
            emailSenderService.sendEmail(mailMessage);

            modelAndView.addObject("message", "Request to reset password received. Check your inbox for the reset link.");
            modelAndView.setViewName("login");

        } else {
            modelAndView.addObject("message", "This email address does not exist!");
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }
    
    
 // Endpoint to confirm the token
    @RequestMapping(value="/confirm-reset", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView validateResetToken(ModelAndView modelAndView, @RequestParam("token")String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (token != null) {
            User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            userRepository.save(user);
            modelAndView.addObject("user", user);
            modelAndView.addObject("emailId", user.getEmail());
            modelAndView.setViewName("resetPassword");
        } else {
            modelAndView.addObject("message", "The link is invalid or broken!");
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    // Endpoint to update a user's password
    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public ModelAndView resetUserPassword(ModelAndView modelAndView, User user) {
        if (user.getEmail() != null) {
            // Use email to find user
            User tokenUser = userRepository.findByEmailIgnoreCase(user.getEmail());
            tokenUser.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(tokenUser);
            modelAndView.addObject("message", "Password successfully reset. You can now log in with the new credentials.");
            modelAndView.setViewName("login");
        } else {
            modelAndView.addObject("message","The link is invalid or broken!");
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }
    

}
