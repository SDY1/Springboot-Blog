package com.cos.blogapp.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.cos.blogapp.domain.user.User;
// 옛날에는 이거를 썻음 extends HandlerInterceptor : 디플리케이트(어뎁터 패턴 안쓰고 인터페이스에서 디폴트를 쓰기 때문에)
public class SessionInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("preHandle 실행됨");
		HttpSession session = request.getSession();
		User principal = (User)session.getAttribute("principal");
		if(principal == null) {
			response.sendRedirect("/loginForm");
		}
		return true;
	}
}
