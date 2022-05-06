package com.lecture.jpausefirst.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.lecture.jpausefirst.domain.Member;
import com.lecture.jpausefirst.repository.MemberRepository;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional //test후 데이터 초기화 이뤄진다.
class MemberServiceTest {

	@Autowired MemberService memberService;
	@Autowired MemberRepository memberRepository;
	@Autowired EntityManager entityManager;

	@Test
	void 회원가입() {
		//given
		Member member = new Member();
		member.setName("kkk");

		//when
		Long savedId = memberService.join(member);
		entityManager.flush();

		//then
		assertThat(member).isEqualTo(memberRepository.findOne(savedId));
	}

	@Test
	void 중복_회원_예외() {
		//given
		Member member1 = new Member();
		member1.setName("kim");

		Member member2 = new Member();
		member2.setName("kim");

		//when
		memberService.join(member1);

		//then
		assertThatThrownBy(() -> memberService.join(member2))
			.isInstanceOf(IllegalStateException.class);
	}
}