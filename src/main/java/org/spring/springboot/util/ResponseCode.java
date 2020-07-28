package org.spring.springboot.util;


import org.spring.springboot.exception.ErrorInfoInterface;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResponseCode implements ErrorInfoInterface {

	SUCCESS(200, "成功"),
	/**
	 * 参数类
	 */
	PARAM_ERROR(1013001, "参数异常"),
	VERIFY_CODE_NUL(1013002, "验证码不能为空"),
	VERIFY_CODE_ERROR(1013003, "您输入的验证码不符"),
	REGESIT_CODE_ERROR(1013003, "请不要重复注冊"),
	BAD_REQUEST(1013004, "请求无效"),
	VERIFY_WX_ERROR(1013005, "微信配置信息无效"),
	SMS_ERROR(17508, "短信验证码发送失败"),
	SANME_USER(17501, "用户已存在"),
	CALL_SERVICE_ERROR(17801, "调用服务失败"),
	OP_PERIODS(17509, "执行间隔太快"),
	CALL_WX_ERROR(17802, "接口调用失败"),


	UPLOADERROR(17805, "上传附件失败"),
	/**
	 * 接口类
	 */
	INTERNAL_SERVER_ERROR(1013006, "服务器内部错误"),
	CALL_REMOTE_API_ERROR(1013007, "调用远程接口失败"),

	NOLOGINERROR(1013008, "请先登录"),

	TYPE_ERROR(1013010, "无效的登陆类型"),
	BUSI_ERROR(1013020, "服务器繁忙,请稍候再试!"),
	VERIFY_FACE_ERROR(1013021, "活体识别!"),

	THUMB_ERROR(1013031, "压缩缩略图失败"),

	INVALID_TOKEN(1013009, "无效的TOKEN");


	private Integer code;
	private String msg;

	ResponseCode(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Override
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
