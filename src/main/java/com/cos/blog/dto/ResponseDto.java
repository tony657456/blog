package com.cos.blog.dto;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ResponseDto<T> {
	private final int status;
	private final T data;
}
