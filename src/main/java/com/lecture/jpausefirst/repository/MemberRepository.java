package com.lecture.jpausefirst.repository;

import com.lecture.jpausefirst.domain.Member;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository //ComponentScan에 의해 자동으로 스프링 빈으로 관리
@RequiredArgsConstructor
public class MemberRepository {

//	@PersistenceContext //스프링이 JPA의 엔티티매니저를 만들어서 주입해준다.
	//spring boot, spring Data JPA를 사용할 경우, @PersistenceContext대신 @Autowired로 사용할 수 있다.
	private final EntityManager entityManager;

	public void save(Member member) {
		entityManager.persist(member);
	}

	public Member findOne(Long id) {
		return entityManager.find(Member.class, id);
	}

	public List<Member> findAll() {
		return entityManager.createQuery("select m from Member m", Member.class)
			.getResultList();
	}

	public List<Member> findByName(String name) {
		return entityManager.createQuery("select m from Member m where m.name = : name", Member.class)
			.setParameter("name", name)
			.getResultList();
	}
}
