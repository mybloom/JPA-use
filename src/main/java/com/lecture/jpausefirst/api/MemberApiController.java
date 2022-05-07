package com.lecture.jpausefirst.api;

import com.lecture.jpausefirst.domain.Member;
import com.lecture.jpausefirst.service.MemberService;
import javax.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MemberApiController {

	private final MemberService memberService;

	@PostMapping("/api/v1/members")
	public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
		Long id = memberService.join(member);
		return new CreateMemberResponse(id);

	}

	@Data
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	static class CreateMemberResponse {
		private Long id;
	}
}
