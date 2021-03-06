package com.lecture.jpausefirst.service;

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
	 *
	 * @param id
	 */
	@Transactional
	public void updateItem(Long itemId, String name, int price, int stockQuantity) {
		Item findItem = itemRepository.findOne(itemId); //영속상태
		findItem.setPrice(price);
		findItem.setName(name);
		findItem.setStockQuantity(stockQuantity);
	}

	public List<Item> findItems() {
		return itemRepository.findAll();
	}

	public Item findOne(Long itemId) {
		return itemRepository.findOne(itemId);
	}

}
