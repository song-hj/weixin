$(function() {
	$('#J_login').on('click', function() {
		window.location.href = '/wxAuth';
	});

	$('#J_pay').on('click', function() {
		$.ajax({
			url: "/wx/pay",
			type: 'post',
			dataType: 'json',
			success: function(response) {
				if (response.code == 1) {
					var charge = response.charge;
					wxpay.createPayment(charge, function(result, error) {
						//支付返回结果
						if (result == "success") {
							alert('发起支付成功');
						} else if (result == "fail") {
							// charge 不正确或者微信公众账号支付失败时会在此处返回
							alert(error);
						} else if (result == "cancel") {
							// 微信公众账号支付取消支付
							alert(error);
						}
					});
				}
			}
		});
	});

	$('#J_share').on('click', function() {
		var url = location.href.split('#')[0];
		$.ajax({
			url: "/wx/share",
			type: 'post',
			dataType: 'json',
			data: {
				'url':url
			},
			success: function(response) {
				if (response && response.code == 1) {
					var charge = response.charge;
					charge.title = '欢迎加入';
					charge.imgUrl =  window.location.protocol + '//'+window.location.host + '/images/share_img.jpg';
					charge.desc = '大吉大利，今晚吃鸡'; //分享朋友圈用
					wxshare.wxshare(charge, function(result) {
						if (result.errMsg == "config:ok") {
							console.log('初始化成功');
						}else{
							alert(result.errMsg);
						}
					});
				}else{
					//失败
					alert(response.message);
				}
			}
		});
	});
});