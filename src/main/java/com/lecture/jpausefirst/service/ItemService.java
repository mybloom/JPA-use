package com.lecture.jpausefirst.service;

import com.lecture.jpausefirst.domain.item.Book;
import com.lecture.jpausefirst.domain.item.Item;
import com.lecture.jpausefirst.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	@Transactional
	public void saveItem(Item item) {
		itemRepository.save(item);
	}

	/**
	 * 준영속 상태의 엔티티 변경을 위한 메서드
	 * @param id
	 */
	@Transactional
	public void updateItem(Long itemId, Book param) {
		Item findItem = itemRepository.findOne(itemId); //영속상태
		findItem.setPrice(param.getPrice());
		findItem.setName(param.getName());
		findItem.setStockQuantity(param.getStockQuantity());

		//transaction에 의해 commit이 되면 , JPA는 flush를 날린다.
		//변경된 것을 DB에 적용한다.

//		itemRepository.save(findItem);// 호출할 필요없다. 왜냐면 findItem이 영속상태이기 때문에
}

	public List<Item> findItems() {
		return itemRepository.findAll();
	}

	public Item findOne(Long itemId) {
		return itemRepository.findOne(itemId);
	}

}
