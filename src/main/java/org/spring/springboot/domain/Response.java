package org.spring.springboot.domain;

import org.spring.springboot.exception.ErrorInfoInterface;
import org.spring.springboot.util.ResponseCode;

/**
 * @author 统一API响应结果封装
 */
public class Response<T> {

	private Integer code;
	private String msg;
	private T data;

	public Response() {
		this.code = ResponseCode.SUCCESS.getCode();
		this.msg = ResponseCode.SUCCESS.getMsg();
	}

	public Response(ErrorInfoInterface errorInfo) {
		this.code = errorInfo.getCode();
		this.msg = errorInfo.getMsg();
	}
	public Response(T data) {
		this.code = ResponseCode.SUCCESS.getCode();
		this.msg = ResponseCode.SUCCESS.getMsg();
		this.data = data;
	}



	public Integer getCode() {
		return code;
	}

	public Response setCode(Integer code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public Response setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
