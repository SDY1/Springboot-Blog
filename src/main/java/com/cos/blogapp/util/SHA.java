package com.cos.blogapp.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA {
	public static String encrypt(String rawPassword, MyAlgorithm algorithm) {
		// 1. SHA256 함수를 가진 클래스 객체 가져오기
		MessageDigest md = null;
		try {  // new한 것을 재사용(싱글톤)
			md = MessageDigest.getInstance(algorithm.getType()); // SHA-256, SHA-512
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		// 2. 비밀번호 1234 -> SHA256 던지기
		md.update(rawPassword.getBytes()); // byte배열로 담음
		
		// 3. 암호화된 글자를 16진수로 변환(헥사코드)
		StringBuilder sb = new StringBuilder();
		for(Byte b:md.digest()) { // 암호화 반환
			sb.append(String.format("%02x",b)); // 16진수로
		}
		return sb.toString();
	}
}
