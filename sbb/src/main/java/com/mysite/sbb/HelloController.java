package com.mysite.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
//	@GetMapping("/")
//	public String root() {
//		return "redirect:hello";
//	}
	
	// redirect : 사용자가 처음 요청한 url이 아닌 다른 url로 보내는 것. 3XX 상태코드를 응답할 때 일어남.
	
	@GetMapping("/hello")
	@ResponseBody
	// @ResponseBody : HTTP 요청의 바디 내용으로 매핑하여 클라이언트로 전송(문자열이나 json 형태 그대로 페이지에 출력)
	// @ResponseBody가 없으면 return 문에 작성한 문자열을 파일명으로 하는 템플릿 파일을 찾게 됨. 
	// 만약 그 템플릿 파일이 존재하지 않으면 에러가 나게 됨.
	public String hello() {
		return "Hello Spring Boot Board";
	}
	
}
