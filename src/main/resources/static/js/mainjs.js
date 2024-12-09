// Bước 10: Render in Ajax
// Tạo file javascript để render API: mainjs.js


//Hàm đăng xuất
$("#logout").click(function() {
	localStorage.clear();
	window.location.href = "/login";
});


$(document).ready(function() {
	//Hiển thị thông tin người dùng đăng nhập thành công
	$.ajax({
		type: 'GET',
		url: '/users/me',
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		beforeSend: function(xhr) {
			if (localStorage.token) {
				xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.token);
			}
		},
		success: function(data) {
			var json = JSON.stringify(data, null, 4);
			// $('#profile').html(json);
			$('#email').html(data.email);
			document.cookie = `userEmail=${data.email}; path=/;`;
			//console.log("SUCCESS : ", data);
			//alert('Hello ' + data.email + '! You have successfully accessed to /api/profile.');
		},
		error: function() {
			var json = e.responseText;
			$('#feedback').html(json);
			//console.log("ERROR : ", e);
			alert("Sorry, you are not logged in.");
		}
	});
});




$("#Login").click(function(event) {
	event.preventDefault();
	var email = document.getElementById('email').value;
	var password = document.getElementById('password').value;
	var basicInfo = JSON.stringify({
		email: email,
		password: password
	});
	$.ajax({
		type: "POST",
		url: "/auth/login",
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		data: basicInfo,
		success: function(data) {
			localStorage.token = data.token;
			// Kiểm tra role và điều hướng tương ứng
			//if (data.role.roleName === "ADMIN") {
			//window.location.href = "/admin";
			//} else if (data.role.roleName === "USER") {
			

			// Xóa cookie cũ nếu có

			// Thiết lập cookie mới
			window.location.href = "/admin2";
			//} else {
			//alert("Unknown role. Access denied.");
			//}
		},
		error: function() {
			alert("Login failed");
		}
	});
});



// Hàm Register
$(document).ready(function() {
	$("#formregister").submit(function(event) {
		// Ngừng sự kiện mặc định của form
		event.preventDefault();

		// Lấy giá trị từ các trường
		var email = $("#email").val();
		var password = $("#password").val();
		var confirmPassword = $("#confirmPassword").val();
		var phone = $("#phone").val();
		var userName = $("#username").val();
		var isValid = true;

		// Reset thông báo lỗi
		$(".help-block").text("");

		if (userName === "") {
			$("#usernameError").text("Username is required.");
			isValid = false;
		}

		if (email === "") {
			$("#emailError").text("Email is required.");
			isValid = false;
		}

		// Kiểm tra độ mạnh của mật khẩu
		var passwordStrengthRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=!./,]).{8,}$/;

		if (password === "") {
			$("#passwordError").text("Password is required.");
			isValid = false;
		} else if (!passwordStrengthRegex.test(password)) {
			$("#passwordError").text(
				"Password must be at least 8 characters long, include at least one uppercase letter, one number, and one special character."
			);
			isValid = false;
		}

		// Kiểm tra xác nhận mật khẩu
		if (confirmPassword === "") {
			$("#confirmPasswordError").text("Confirm password is required.");
			isValid = false;
		} else if (password !== confirmPassword) {
			$("#confirmPasswordError").text("Passwords do not match.");
			isValid = false;
		}

		// Kiểm tra số điện thoại
		var phoneRegex = /^0\d{9}$/; // Bắt đầu bằng '0' và có đúng 10 chữ số
		if (phone === "") {
			$("#phoneError").text("Phone number is required.");
			isValid = false;
		} else if (!phoneRegex.test(phone)) {
			$("#phoneError").text("Invalid phone number. It must start with '0' and contain exactly 10 digits.");
			isValid = false;
		}

		// Nếu có lỗi, dừng lại
		if (!isValid) {
			return;
		}

		// Kiểm tra email và số điện thoại đã tồn tại
		$.ajax({
			type: "POST",
			url: "/auth/check-existence",  // API kiểm tra email và số điện thoại
			dataType: "json",
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify({ email: email, phone: phone }),
			success: function(response) {
				// Kiểm tra kết quả trả về từ server
				if (response.emailExists) {
					$("#emailError").text("Email already exists.");
					return;  // Dừng lại nếu email đã tồn tại
				}

				if (response.phoneExists) {
					$("#phoneError").text("Phone number already exists.");
					return;  // Dừng lại nếu số điện thoại đã tồn tại
				}

				// Nếu email và số điện thoại đều không tồn tại, tiếp tục đăng ký


				// Thiết lập giá trị mặc định cho role và status
				var registerInfo = JSON.stringify({
					email: email,
					password: password,
					userName: userName,
					phone: phone,
					role: {
						roleId: 2, // Giá trị cố định cho role
						roleName: "USER" // Giá trị cố định cho role
					},
					status: 1 // Giá trị cố định cho status
				});

				// Gửi yêu cầu AJAX để đăng ký người dùng
				$.ajax({
					type: "POST",
					url: "/auth/signup",
					dataType: "json",
					contentType: "application/json; charset=utf-8",
					data: registerInfo,
					success: function(data) {
						alert("Registration successful! You can now log in.");
						window.location.href = "/login";  // Điều hướng đến trang đăng nhập
					},
					error: function(xhr, status, error) {
						console.error("Error:", error);
						alert("Registration failed. Please try again.");
					}
				});
			},
			error: function(xhr, status, error) {
				console.error("Error:", error);
				alert("Error occurred while checking for existing email or phone.");
			}
		});
	});
});







