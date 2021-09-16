package com.cos.blogapp.test;

// 1.8 람다식
// 1. 함수를 넘기는 게 목적
// 2. 인터페이스에 함수가 무조건 하나여야 함
// 3. 쓰면 코드가 간결해지고, 타입을 몰라도 됨

interface MySupplier{
	void get();
}
public class LamdaTest {
	
	static void start(MySupplier s) {
		s.get();
	}
	
	public static void main(String[] args) {
		start(() -> {System.out.println("get함수 호출됨");}); // s의 get함수 구현됨. 함수로 넘기기 가능(함수가 하나일 때)
	}
}
