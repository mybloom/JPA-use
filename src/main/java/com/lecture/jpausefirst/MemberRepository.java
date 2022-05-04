package com.lecture.jpausefirst;

import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

	private final EntityManager entityManager;

	public Long save(Member member) {
		entityManager.persist(member);
		return member.getId();
	}

	public Member find(Long id) {
		return entityManager.find(Member.class, id);
	}
}
