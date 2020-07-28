package org.spring.springboot.exception;

/**
 * @author
 */
public class GlobalException extends RuntimeException {

	private ErrorInfoInterface errorInfo;

	public GlobalException(ErrorInfoInterface errorInfo) {
		this.errorInfo = errorInfo;
	}

	public ErrorInfoInterface getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(ErrorInfoInterface errorInfo) {
		this.errorInfo = errorInfo;
	}

}