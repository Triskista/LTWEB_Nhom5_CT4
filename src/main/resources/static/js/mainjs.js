// Bước 10: Render in Ajax
// Tạo file javascript để render API: mainjs.js



//Hàm đăng xuất
$("#logout").click(function() {
	localStorage.clear();
	window.location.href = "/login";
});

//Hàm Login
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
			//alert("Got a token from the server! Token: " + data.token);
			window.location.href = "/user/profile";
		},
		error: function() {
			alert("Login failed");
		}
	});
});