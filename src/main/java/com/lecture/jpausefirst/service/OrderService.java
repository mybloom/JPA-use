package com.lecture.jpausefirst.service;

import com.lecture.jpausefirst.domain.Delivery;
import com.lecture.jpausefirst.domain.Member;
import com.lecture.jpausefirst.domain.Order;
import com.lecture.jpausefirst.domain.OrderItem;
import com.lecture.jpausefirst.domain.item.Item;
import com.lecture.jpausefirst.repository.ItemRepository;
import com.lecture.jpausefirst.repository.MemberRepository;
import com.lecture.jpausefirst.repository.OrderRepository;
import com.lecture.jpausefirst.repository.OrderSearch;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final MemberRepository memberRepository;
	private final ItemRepository itemRepository;

	/**
	 * 주문
	 *
	 * @param memberId
	 * @param itemId
	 * @param count
	 * @return
	 */
	@Transactional
	public Long order(Long memberId, Long itemId, int count) {
		//엔티티 조회
		Member member = memberRepository.findOne(memberId);
		Item item = itemRepository.findOne(itemId);

		//배송정보 생성
		Delivery delivery = new Delivery();
		delivery.setAddress(member.getAddress());

		//주문상품 생성
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

		//주문 생성
		Order order = Order.createOrder(member, delivery, orderItem);

		//주문 저장
		//cascade = CascadeType.ALL을 oderItem과 delivery에서 해줬기 때문에 order만 save해도 연관관계 모두 저장된다.
		orderRepository.save(order);

		return order.getId();
	}

	/**
	 * 주문 취소
	 * @param orderId
	 */
	@Transactional
	public void cancelOrder(Long orderId) {
		//주문 엔티티 조회
		Order order = orderRepository.findOne(orderId);
		//주문 취소
		order.cancel();
	}

	/**
	 * 검색
	 * @param orderSearch
	 * @return
	 */
	public List<Order> findOrders(OrderSearch orderSearch) {
		return orderRepository.findAllString(orderSearch);
	}

}
