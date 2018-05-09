var wxshare = {
		_resultCallback : undefined,
		createPayment :function(charge_json, callback){
			if (typeof callback == "function") {
				this._resultCallback = callback;
			}
			var charge = charge_json;
			if (typeof charge_json == "string") {
				charge = JSON.parse(charge_json);
			} else {
				charge = charge_json;
			}
			wx.config({
				"debug": false, 
				"appId" : charge.appId, // 公众号名称，由商户传入
				"timestamp" : charge.timeStamp, // 时间戳，自1970年以来的秒数
				"nonceStr" : charge.nonceStr, // 随机串
				"signature" : charge.signature,
				"jsApiList" : [
				               "onMenuShareTimeline",//分享到朋友圈
				               "onMenuShareAppMessage"//分享给朋友
				               ]
			});
			wx.ready(function(){
				//分享到朋友圈
				wx.onMenuShareTimeline({
					"title": charge.title, // 分享标题
					"link": charge.link, // 分享链接
					"imgUrl": charge.imgUrl, // 分享图标
					success: function () {
						// 用户确认分享后执行的回调函数
					},
					cancel: function () {
						// 用户取消分享后执行的回调函数
					}
				});
				//分享给朋友
				wx.onMenuShareAppMessage({
					title: charge.title, // 分享标题
					desc: charge.desc, // 分享描述
					link: charge.link, // 分享链接
					imgUrl: charge.imgUrl, // 分享图标
					type: '', // 分享类型,music、video或link，不填默认为link
					dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
					success: function () {
						// 用户确认分享后执行的回调函数
					},
					cancel: function () {
						// 用户取消分享后执行的回调函数
					}
				});
			});
			//错误信息
			wx.error(function (res) {
				//console.log(res.errMsg);
				this._resultCallback(res);
			});
		}
}