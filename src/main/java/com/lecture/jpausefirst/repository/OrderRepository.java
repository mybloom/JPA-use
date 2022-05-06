package com.lecture.jpausefirst.repository;

import com.lecture.jpausefirst.domain.Order;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

	private final EntityManager entityManager;

	public void save(Order order){
		entityManager.persist(order);
	}

	public Order findOne(Long id) {
		return entityManager.find(Order.class, id);
	}

	/**
	 * 문자열로 동적 쿼리 생성
	 * @param orderSearch
	 * @return
	 */
	public List<Order> findAllString(OrderSearch orderSearch) {
		String jpql = "select o from Order o join o.member m";
		boolean isFirstCondition = true;

		//주문 상태 검색
		if(orderSearch.getOrderStatus() != null) {
			if(isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " o.status = :status";
		}

		//회원 상태 검색
		if(StringUtils.hasText((CharSequence) orderSearch.getMemberName())) {
			if(isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " m.name like :name";
		}

		TypedQuery<Order> query = entityManager.createQuery(jpql, Order.class)
			.setMaxResults(1000);  //최대 1000건

		if(orderSearch.getOrderStatus() != null) {
			query = query.setParameter("status", orderSearch.getOrderStatus());
		}
		if(StringUtils.hasText((CharSequence) orderSearch.getMemberName())) {
			query = query.setParameter("name", orderSearch.getMemberName());
		}

		return query.getResultList();
	}


	/**
	 * JPA Criteria <br>
	 * JPA가 표준 지원하는 동적쿼리 생성 - 권장 방법 아님
	 * @param orderSearch
	 * @return
	 */
	public List<Order> findAllCriteria(OrderSearch orderSearch) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root<Order> order = criteriaQuery.from(Order.class); //시작하는 엔티티
		Join<Object, Object> member = order.join("member", JoinType.INNER);

		List<Predicate> criteria = new ArrayList<>();

		//주문상태 검색
		if(orderSearch.getOrderStatus() != null ) {
			Predicate status = criteriaBuilder.equal(order.get("status"),
				orderSearch.getOrderStatus());
			criteria.add(status);
		}

		//회원이름 검색
		if(StringUtils.hasText((CharSequence) orderSearch.getMemberName()) ) {
			Predicate name = criteriaBuilder.like(member.get("name"),
				"%" + orderSearch.getMemberName() + "%");
			criteria.add(name);
		}

		criteriaQuery.where(criteriaBuilder.and(criteria.toArray(new Predicate[criteria.size()])));
		TypedQuery<Order> query = entityManager.createQuery(criteriaQuery)
			.setMaxResults(1000);
		return query.getResultList();
	}

}
