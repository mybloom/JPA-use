package com.lecture.jpausefirst.domain;

import static javax.persistence.FetchType.LAZY;

import com.lecture.jpausefirst.domain.item.Item;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

	@Id
	@GeneratedValue
	@Column(name = "order_item_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	private int orderPrice; //주문 가격

	private int count; //주문 수량

	//==생성 메서드==//

	/**
	 * 주문상품 생성 <br>
	 * 주의 : 할인등이 적용된 가격이 있을 수 있으므로, item.price를 사용하지 않고 orderPrice를 인자로 받는다.
	 * @param item
	 * @param orderPrice
	 * @param count
	 * @return
	 */
	public static OrderItem createOrderItem(Item item , int orderPrice, int count){
		OrderItem orderItem = new OrderItem();
		orderItem.setItem(item);
		//할인등이 적용된 가격이 있을 수 있으므로, item.price를 사용하지 않고 orderPrice를 인자로 받는다.
		orderItem.setOrderPrice(orderPrice);
		orderItem.setCount(count);

		item.removeStock(count);
		return orderItem;
	}

	//==비지니스 로직==//
	/**
	 * 주문상품 취소 <br>
	 * 재고수량 더해준다
	 */
	public void cancel() {
		getItem().addStock(count);
	}

	//==조회 로직==//
	/**
	 * 주문상품 전체 가격 조회
	 * @return
	 */
	public int getTotalPrice() {
		return getOrderPrice() * getCount();
	}
}
