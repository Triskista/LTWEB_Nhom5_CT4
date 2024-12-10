/* --------------------www.bootstrappage.com----------------------------------- */
$('#ourServices .thumbnail').mouseenter(function() {
	$(this).css('background-color', '#FE5214');
	$(this).animate({
		opacity: 1
	}, 200, function() {
		// Animation complete.
	});
});
$('#ourServices .thumbnail').mouseleave(function() {
	$(this).css('background-color', '#ffffff');
	$(this).animate({
		opacity: 0.8

	}, 0, function() {
		// Animation complete.
	});
});

// Hàm để điều hướng menu khi nhấn vào avatar
function toggleMenu() {
	var menu = document.getElementById('dropdown-menu');
	menu.style.display = (menu.style.display === 'block') ? 'none' : 'block';
}





// Apply Bootstrap Scrollspy to show active navigation link based on page scrolling
$('.navbar').scrollspy();

// Scroll page with easing effect
$('.navbar ul li a').bind('click', function(e) {
	e.preventDefault();
	target = this.hash;
	$.scrollTo(target, 1500, {
		easing: 'easeOutCubic'
	});

	$(".btn-navbar").click();
});

// Scroll page with easing effect
$('#footerMenu a').bind('click', function(e) {
	e.preventDefault();
	target = this.hash;
	$.scrollTo(target, 1500, {
		easing: 'easeOutCubic'
	});

	$(".btn-navbar").click();
});



// Show/Hide Sticky "Go top" button
$(window).scroll(function() {
	if ($(this).scrollTop() > 200) {
		$(".go-top").fadeIn(200);
	}
	else {
		$(".go-top").fadeOut(200);
	}
});
$(document).ready(function() {
	$('.nav a').on('click', function(e) {
		e.preventDefault(); // Ngăn chặn hành động mặc định của liên kết.
		var target = $(this).attr('href'); // Lấy giá trị href (ví dụ: #carouselSection).
		if ($(target).length) {
			$('html, body').animate(
				{
					scrollTop: $(target).offset().top, // Tính vị trí mục tiêu và cuộn đến đó.
				},
				800 // Thời gian cuộn (tính bằng ms).
			);
		}

		// Đặt lại trạng thái active cho mục đã click.
		$('.nav li').removeClass('active'); // Bỏ class "active" khỏi tất cả các mục.
		$(this).parent().addClass('active'); // Thêm class "active" cho mục được click.
	});

	// Đóng menu nếu đang ở chế độ mobile.
	$('.btn-navbar').on('click', function() {
		$('.nav-collapse').toggleClass('collapse');
	});
});

// Scroll Page to Top when clicked on "go top" button
$(".brand, .go-top").click(function(event) {
	event.preventDefault();

	$.scrollTo('#carouselSection', 1500, {
		easing: 'easeOutCubic'
	});
});



$(function() {
	$('#mainCarousel').carousel('cycle');

	$('#btnNew').click(function(e) {
		$('.all').quicksand($('.mgmt li'), {
			duration: 500,
			attribute: 'id',
			easing: 'easeInOutQuad'
		});
		$("#liNew").attr("class", "active");
		$("#liComingsoon").attr("class", "");
		$("#liPopular").attr("class", "");
		$("#liAll").attr("class", "");
		e.preventDefault();
	});
	$('#btnPopular').click(function(e) {
		$('.all').quicksand($('.designs li'), {
			duration: 500,
			attribute: 'id',
			easing: 'easeInOutQuad'
		});
		$("#liNew").attr("class", "");
		$("#liAll").attr("class", "");
		$("#liComingsoon").attr("class", "");
		$("#liPopular").attr("class", "active");
		e.preventDefault();
	});

	$('#btnComingsoon').click(function(e) {
		$('.all').quicksand($('.hacks li'), {
			duration: 500,
			attribute: 'id',
			easing: 'easeInOutQuad'
		});
		$("#liNew").attr("class", "");
		$("#liAll").attr("class", "");
		$("#liComingsoon").attr("class", "active");
		$("#liPopular").attr("class", "");
		e.preventDefault();
	});
	$('#btnAll').click(function(e) {
		$('.all').quicksand($('.new li'), {
			duration: 500,
			attribute: 'id',
			easing: 'easeInOutQuad'
		});
		$("#liNew").attr("class", "");
		$("#liAll").attr("class", "active");
		$("#liComingsoon").attr("class", "");
		$("#liPopular").attr("class", "");
		e.preventDefault();
	});

	$('body').tooltip({
		selector: '[rel=tooltip]',
		placement: 'bottom'
	});

	$('LI h4').click(function(e) {
		e.preventDefault(); // disable text selection
		$(this).parent().find('span').slideToggle();
		return false; // disable text selection
	});

	$('#search').keyup(function(e) {
		var s = $(this).val().trim();
		if (s === '') {
			$('#result LI').show();
			return true;
		}
		$('#result LI:not(:contains(' + s + '))').hide();
		$('#result LI:contains(' + s + ')').show();
		return true;
	});


});
// hiện sản phẩm 
document.querySelectorAll('.nav-link').forEach(tab => {
	tab.addEventListener('click', function(event) {
		event.preventDefault();
		const categoryName = this.getAttribute('data-category');

		fetch(`/user/index/${categoryName}`)
			.then(response => response.json())
			.then(data => {
				// Render sản phẩm từ dữ liệu trả về
				updateProducts(data);
			})
			.catch(err => console.error('Error:', err));
	});
});

function updateProducts(products) {
	// Logic để cập nhật UI với danh sách sản phẩm mới
}


