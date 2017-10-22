package com.example.demo.utils.smsUtil;

/**
 * 短信状态等信息
 * 
 * @author Tiansheng.Cheng
 */
public enum SmsEnum {

	PHONE_ERROR("手机号格式错误"),

	/**
	 * 短信发送状态 -> 成功
	 */
	SEND_STATUS_SUCCESS("0"),
	
	/**
	 * 短信发送状态 -> 失败
	 */
	SEND_STATUS_FAIL("1"),
	
	/**
	 * 是否验证 -> 已验
	 */
	IS_VALID_Y("Y"),
	
	/**
	 * 是否验证 -> 未验
	 */
	IS_VALID_N("N"),
	
	/**
	 * 短信渠道名称
	 */
	SMS_CHANNEL_U_NAME("nnjinrong"),
	
	/**
	 * 短信渠道密码
	 */
	SMS_CHANNEL_PWD("eed8f8738b59cf892b44d7271d191784"),
	
	/**
	 * 短信渠道地址
	 */
	SMS_CHANNEL_URL("http://www.smsbao.com/sms"),
	
	/**
	 * 短信模板 -> 注册
	 */
	CONTENT_TEMPLATE_REGISTER("【海湾汽车公园项目】您的操作验证码是：AUTH_CODE，此验证码1分钟内有效。");
	
	private String value;

	private SmsEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
