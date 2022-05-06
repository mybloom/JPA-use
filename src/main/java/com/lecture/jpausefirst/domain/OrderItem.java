package com.lecture.jpausefirst.domain;

import static javax.persistence.FetchType.LAZY;

import com.lecture.jpausefirst.domain.item.Item;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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


	//==비지니스 로직==//
	public void cancel() {
		getItem().addStock(count);
	}

	public int getTotalPrice() {
		return getOrderPrice() * getCount();
	}
}
