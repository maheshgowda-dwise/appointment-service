package com.lifetrenz.lths.appointment.service;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.lifetrenz.lths.appointment.dto.TokenPayLoad;

@Service
public interface TokenService {

	TokenPayLoad getTokenPayload(HttpHeaders httpHeaders) throws Exception;

}
