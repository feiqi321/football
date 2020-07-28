package org.spring.springboot.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.springboot.util.ResponseCode;

/**
 * @author
 */
public class CustomerException extends RuntimeException implements ErrorInfoInterface {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private Integer code;

	private String msg;

	public CustomerException(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
		logger.error(msg);
	}

	public CustomerException(ResponseCode rsc, String msg) {
		this.code = rsc.getCode();
		this.msg = msg;
		logger.error(msg);
	}

	public CustomerException(Integer code, String msg, String param) {
		this.code = code;
		this.msg = String.format(msg, param);
		logger.error(this.msg);
	}

	@Override
	public Integer getCode() {
		return this.code;
	}

	@Override
	public String getMsg() {
		return this.msg;
	}
}
