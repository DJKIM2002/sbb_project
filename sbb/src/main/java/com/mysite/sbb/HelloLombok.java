package com.mysite.sbb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class HelloLombok {
	private final String hello;
	private final int lombok;
	// 클래스 속성을 정의한 코드에 final 키워드(이 키워드가 붙으면 값의 재할당을 막는다.)가 없다면 생성자에 포함되지 않음
	// final을 적용하면 해당 속성값을 변경할 수 없으므로 @Setter는 의미가 없어진다.
	// final은 변수, 메서드. 클래스에 모두 붙일 수 있으며, final이 붙게 되면 값의 재할당, 상속, 오버라이딩이 불가능해진다. 
	// @RequiredArgsConstructor : "특정 인수"가 필요한 생성자 생성 어노테이션
	// @AllArgsConstructor : 해당 클래스의 "모든 클래스 변수"를 "인수"로 가지는 생성자 생성 어노테이션
	// @NoArgsConstructor : "기본 생성자(인수가 필요하지 않은 생성자)" 생성 어노테이션
	
	public static void main(String[] args) {
		HelloLombok helloLombok = new HelloLombok("헬로", 5);
		
		System.out.println(helloLombok.getHello());
		System.out.println(helloLombok.getLombok());
	}
}
