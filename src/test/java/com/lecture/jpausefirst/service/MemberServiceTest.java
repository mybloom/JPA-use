package com.lecture.jpausefirst.service;

import com.lecture.jpausefirst.domain.Member;
import com.lecture.jpausefirst.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional //test후 데이터 초기화 이뤄진다.
class MemberServiceTest {

	@Autowired MemberService memberService;
	@Autowired MemberRepository memberRepository;

	@Test
	@Rollback(value = false)
	void 회원가입() {
		//given
		Member member = new Member();
		member.setName("kkk");

		//when
		Long savedId = memberService.join(member);

		//then
		Assertions.assertThat(member).isEqualTo(memberRepository.findOne(savedId));
	}

	@Test
	void 중복_회원_예외() {
		//given

		//when

		//then

	}
}