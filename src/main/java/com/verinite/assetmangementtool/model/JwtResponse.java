package com.verinite.assetmangementtool.model;

import java.io.Serializable;

public class JwtResponse<T> implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
	private final T data;

	public JwtResponse(String jwttoken) {
		this.jwttoken = jwttoken;
		this.data=null;
	}

	public JwtResponse(String token, T data) {
		this.jwttoken=token;
		this.data=data;
	}

	public T getData(){return this.data;}
	public String getToken() {
		return this.jwttoken;
	}
}