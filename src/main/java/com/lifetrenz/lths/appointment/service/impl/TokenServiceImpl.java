package com.lifetrenz.lths.appointment.service.impl;

import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.service.TokenService;

@Component
public class TokenServiceImpl implements TokenService {

	@Override
	public TokenPayLoad getTokenPayload(HttpHeaders httpHeaders) {
		String token = httpHeaders.getFirst(HttpHeaders.AUTHORIZATION);

		if (token == null || !token.startsWith("Bearer ")) {
			throw new IllegalArgumentException("Invalid or missing Authorization header");
		}

		String[] jwtToken = token.split(" ");
		if (jwtToken.length != 2) {
			throw new IllegalArgumentException("Malformed Authorization token");
		}

		String[] chunks = jwtToken[1].split("\\.");
		if (chunks.length < 2) {
			throw new IllegalArgumentException("Invalid JWT token structure");
		}

		Base64.Decoder decoder = Base64.getUrlDecoder();
		String payload;

		try {
			payload = new String(decoder.decode(chunks[1]));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Failed to decode JWT payload", e);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(payload, TokenPayLoad.class);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Failed to parse JWT payload into TokenPayLoad", e);
		}
	}

}
