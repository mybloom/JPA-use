package com.lecture.jpausefirst.api;

import com.lecture.jpausefirst.domain.Member;
import com.lecture.jpausefirst.service.MemberService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MemberApiController {

	private final MemberService memberService;

	public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {

	}
}
