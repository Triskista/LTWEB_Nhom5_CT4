package vn.iotstar.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import vn.iotstar.entity.User;
import vn.iotstar.models.LoginResponse;
import vn.iotstar.models.LoginUserModel;
import vn.iotstar.models.RegisterUserModel;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.AuthenticationService;
import vn.iotstar.service.JwtService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
	private final JwtService jwtService;
	private final AuthenticationService authenticationService;
	private final UserRepository userRepository;

	public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService,
			UserRepository userRepository) {
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
		this.userRepository = userRepository;
	}

	// Kiểm tra email và số điện thoại có tồn tại không
	@PostMapping("/check-existence")
	public ResponseEntity<Map<String, Boolean>> checkExistence(@RequestBody Map<String, String> data) {
		String email = data.get("email");
		String phone = data.get("phone");
		boolean emailExists = userRepository.findByEmail(email).isPresent();
		boolean phoneExists = userRepository.findByPhone(phone) != null;

		Map<String, Boolean> response = new HashMap<>();
		response.put("emailExists", emailExists);
		response.put("phoneExists", phoneExists);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/signup")
	@Transactional
	public ResponseEntity<User> register(@RequestBody RegisterUserModel registerUser) {
		boolean emailExists = authenticationService.existsByEmail(registerUser.getEmail());
		boolean phoneExists = authenticationService.existsByPhone(registerUser.getPhone());
		if (emailExists) {
			// Trả về ResponseEntity<User> với một đối tượng User rỗng và thông báo lỗi
			User errorUser = new User();
			errorUser.setEmail(registerUser.getEmail()); // Gán email đã tồn tại cho đối tượng lỗi
			return ResponseEntity.badRequest().body(errorUser);
		}

		if (phoneExists) {
			// Trả về ResponseEntity<User> với một đối tượng User rỗng và thông báo lỗi
			User errorUser = new User();
			errorUser.setPhone(registerUser.getPhone()); // Gán số điện thoại đã tồn tại cho đối tượng lỗi
			return ResponseEntity.badRequest().body(errorUser);
		}
		// Nếu không trùng lặp, tiếp tục đăng ký
		User registeredUser = authenticationService.signup(registerUser);
		return ResponseEntity.ok(registeredUser);
	}

	@PostMapping(path = "/login")
	@Transactional
	public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserModel loginUser, HttpServletResponse response) {
		User authenticatedUser = authenticationService.authenticate(loginUser);
		String jwtToken = jwtService.generateToken(authenticatedUser);
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(jwtToken);
		loginResponse.setExpiresIn(jwtService.getExpirationTime());
		Cookie userEmailCookie = new Cookie("userEmail", authenticatedUser.getEmail());

		// Đặt thời gian sống của cookie (tính bằng giây)
		userEmailCookie.setMaxAge(60 * 60 * 24); // Cookie sống trong 1 ngày

		// Đặt cookie chỉ có hiệu lực trong ứng dụng của bạn
		userEmailCookie.setPath("/");

		// Thêm cookie vào phản hồi
		response.addCookie(userEmailCookie);

		return ResponseEntity.ok(loginResponse);
	}
}