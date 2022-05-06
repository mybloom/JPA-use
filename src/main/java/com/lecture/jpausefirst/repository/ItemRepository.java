package com.lecture.jpausefirst.repository;

import com.lecture.jpausefirst.domain.item.Item;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

	private final EntityManager entityManager;

	public void save(Item item) {
		if (item.getId() == null) {  //id값이 없다는 것은 새로 생성하는 객체라는 것.
			entityManager.persist(item);
		} else { //id 값이 있다는 것은 db에 등록된 것을 가져온다는 것.
			entityManager.merge(item);
		}
	}

	public Item findOne(Long id) {
		return entityManager.find(Item.class, id);
	}

	public List<Item> findAll() {
		return entityManager.createQuery("select i from Item i", Item.class)
			.getResultList();
	}
}
