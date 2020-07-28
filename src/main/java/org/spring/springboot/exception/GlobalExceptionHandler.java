package org.spring.springboot.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.springboot.domain.Response;
import org.spring.springboot.util.ResponseCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(value = GlobalException.class)
	public Response errorHandlerOverJson(HttpServletRequest request, HttpServletResponse httpResponse, GlobalException exception) {
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		ErrorInfoInterface errorInfo = exception.getErrorInfo();
		Response response = new Response(errorInfo);
		logger.info("错误信息",exception);
		return response;
	}


	@ExceptionHandler(value = CustomerException.class)
	public Response errorHandlerOverJson(HttpServletRequest request, HttpServletResponse httpResponse, CustomerException exception) {
		logger.info("异常显示{}",exception);
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		Response result = new Response();
		result.setCode(ResponseCode.BAD_REQUEST.getCode());
		result.setMsg(exception.getCause().getMessage());
		return result;
	}

	@ExceptionHandler(value = Exception.class)
	public Response handleException(HttpServletRequest request, HttpServletResponse httpResponse,Exception exception) {
		logger.info("异常显示{}",exception);
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		exception.printStackTrace();
		Response result = new Response();
		result.setCode(ResponseCode.BAD_REQUEST.getCode());
		result.setMsg(exception.getCause().getMessage());
		return result;
	}


}
