package com.lifetrenz.lths.appointment.common.app;

import java.io.Serializable;

public class ApplicationResponse<T> implements Serializable {

	private String status;
	private String code;
	private String message;
	private T data;
	
	private static final long serialVersionUID = 9127529345019833450L;

	public ApplicationResponse() {
		super();
	}
	
	public ApplicationResponse(String status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
	
	public ApplicationResponse(String status, String code, String message, T data) {
		this.status = status;
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
