package com.cos.blogapp.util;

import lombok.Getter;

@Getter
public enum MyAlgorithm { // 열거형 타입
	SHA256("SHA-256"), SHA512("SHA-512");
	
	String type;
	
	private MyAlgorithm(String type) {
		this.type = type;
	}
}
