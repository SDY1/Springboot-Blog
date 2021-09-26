package com.cos.blogapp.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMRespDto<T> {   // 통신 하려면 json데이터로 넘겨주는게 다루기 좋음
	private int code; // 1 성공, -1 실패
	private String msg;
	private T body; // 상황에 맡게 처리 하기 위해서 지네릭 사용
}
