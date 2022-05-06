package com.lecture.jpausefirst.service;

import com.lecture.jpausefirst.domain.Member;
import com.lecture.jpausefirst.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) //기본으로 readOnly true로 쓰고, 읽기 이외의 메서드에 따로 추가
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;


	/**
	 * 회원가입
	 * @param member
	 * @return
	 */
	@Transactional //readOnly false가 기본
	public Long join(Member member) {
		//중복회원 검증
		validateDuplicateMember(member);
		memberRepository.save(member);
		return member.getId(); //영속성 컨텍스트에 넣는 시점에 pk가 생성되기 때문에 getId()가 항상 있는 것이 보장된다.
	}

	/**
	 * 중복회원 검증 <br>
	 * 멀티쓰레드 환경에서는 동시에 같은 이름으로 회원 가입시 중복이 될 수 있으므로,
	 * 안전하게 하기 위해 DB에 unique 제약조건을 걸어둔다.
	 * @param member
	 */
	private void validateDuplicateMember(Member member) {

		List<Member> findMembers = memberRepository.findByName(member.getName());
		if(!findMembers.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}

	/**
	 * 회원 전체 조회
	 * @return
	 */
	public List<Member> findMembers() {
		return memberRepository.findAll();
	}

	public Member FindOne(Long memberId) {
		return memberRepository.findOne(memberId);
	}

}
