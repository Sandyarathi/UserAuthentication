package com.ct.exceptions;

public class AuthenticationException extends RuntimeException {

	private static final long serialVersionUID = 4726822802435896345L;

	public AuthenticationException(String msg) {
		super(msg);
	}

}
