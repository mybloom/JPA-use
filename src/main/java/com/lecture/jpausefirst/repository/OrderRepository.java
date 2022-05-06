package com.lecture.jpausefirst.repository;

import com.lecture.jpausefirst.domain.Order;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

	private final EntityManager entityManager;

	public void save(Order order){
		entityManager.persist(order);
	}

	public void findOne(Long id) {
		entityManager.find(Order.class, id);
	}

	/*public List<Order> findAll(OrderSearch orderSearch) {

	}*/
}
