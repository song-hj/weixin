var wxpay = {
	_resultCallback : undefined,
	createPayment : function(charge_json, callback) {
		if (typeof callback == "function") {
			this._resultCallback = callback;
		}
		var charge;
		if (typeof charge_json == "string") {
			try {
				charge = JSON.parse(charge_json);
			} catch (err) {
				this._innerCallback("fail", this._error("json_decode_fail"));
				return;
			}
		} else {
			charge = charge_json;
		}
		if (typeof charge == "undefined") {
			this._innerCallback("fail", this._error("json_decode_fail"));
			return;
		}
		
		_this = this;
		function onBridgeReady() {
			WeixinJSBridge.invoke('getBrandWCPayRequest', {
				"appId" : charge.appId, // 公众号名称，由商户传入
				"timeStamp" : charge.timeStamp, // 时间戳，自1970年以来的秒数
				"nonceStr" : charge.nonceStr, // 随机串
				"package" : charge.package,
				"signType" : charge.signType, // 微信签名方式：
				"paySign" : charge.paySign
			// 微信签名
			}, function(res) {
				if (res.err_msg == 'get_brand_wcpay_request:ok') {
					_this._innerCallback("success");
				} else if (res.err_msg == 'get_brand_wcpay_request:cancel') {
					_this._innerCallback("cancel");
				} else {
					_this._innerCallback("fail", self._error("wx_result_fail",
							res.err_msg));
				}
			});
		}
		if (typeof WeixinJSBridge == "undefined") {
			if (document.addEventListener) {
				document.addEventListener('WeixinJSBridgeReady', onBridgeReady,
						false);
			} else if (document.attachEvent) {
				document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
				document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
			}
		} else {
			onBridgeReady();
		}
	},
	_innerCallback : function(result, err) {
		if (typeof this._resultCallback == "function") {
			if (typeof err == "undefined") {
				//err = this._error();
			}
			this._resultCallback(result, err);
		}
	}
}