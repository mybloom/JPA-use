package com.lecture.jpausefirst;


import static org.assertj.core.api.Assertions.*;

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
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

	@Test
	@Transactional
	@Rollback(value = false)
	void testMember() throws Exception {
		//given
		Member member = new Member();
		member.setUserName("memberA");

		//when
		Long savedId = memberRepository.save(member);
		Member findMember = memberRepository.find(member.getId());

		//then
		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUserName()).isEqualTo(member.getUserName());
		assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성
	}
}