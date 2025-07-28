package com.verinite.assetmanagementtool.response;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class JwtResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -8091879091924046844L;
    private final String token;
    private final T data;

    public JwtResponse(String jwtToken) {
        this.token = jwtToken;
        this.data = null;
    }

    public JwtResponse(String token, T data) {
        this.token = token;
        this.data = data;
    }

}