package com.cos.blogapp.util;

import lombok.Getter;

// 열거형 (카테고리 정할 때, 범주가 정해져 있을 때)
// -> 실수하지 않기 위해 사용(db - 일 -> sun)
@Getter
public enum MyAlgorithm { // 열거형 타입
	SHA256("SHA-256"), SHA512("SHA-512");
	
	String type;
	
	private MyAlgorithm(String type) {
		this.type = type;
	}
}
