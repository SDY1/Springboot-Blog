package com.cos.blogapp.util;

public class Script {

	public static String back(String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("alert('" + msg + "');");
		sb.append("history.back();"); // 뒤로가기하면 데이터가 안날아감
		sb.append("</script>");
		
		return sb.toString();
	}
	
	public static String href(String path) {
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("location.href='" + path + "';");
		sb.append("</script>");
		
		return sb.toString();
	}
	
	public static String href(String path, String msg) { // 오버로딩해서 alert기능 추가
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("alert('" + msg + "');");
		sb.append("location.href='" + path + "';");
		sb.append("</script>");
		
		return sb.toString();
	}
}
