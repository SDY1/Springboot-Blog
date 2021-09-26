package com.cos.blogapp.handler.ex;
// /** + enter
/**
 * 
 * @author SDY 2021.09.16
 * 1. id를 못찾았을 때 사용
 *
 */
// 예외가 너무 많기 때문에 내가 만들어서 분류(보통 4가지 정도)
public class MyAsyncNotFoundException extends RuntimeException{
	
	public MyAsyncNotFoundException(String msg) {
		super(msg);
	}

}
