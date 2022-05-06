package com.lecture.jpausefirst.domain;

import static javax.persistence.FetchType.LAZY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@NoArgsConstructor //(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Order {

	@Id
	@GeneratedValue
	@Column(name = "order_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //order 저장하면 orderItems도 같이 persist된다.
	private List<OrderItem> orderItems = new ArrayList<>();

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "delivery_id") //1:1의 주인으로 설정
	private Delivery delivery;

	private LocalDateTime orderDate;

	private OrderStatus status;

	//연관관계 편의 메서드
	public void setMember(Member member) {
		this.member = member;
		member.getOrders().add(this);
	}

	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
		orderItem.setOrder(this);
	}

	public void addDelivery(Delivery delivery) {
		this.delivery = delivery;
		delivery.setOrder(this);
	}

	//==생성 메서드 ==/

	/**
	 * 주문 생성 <br>
	 * Order의 연관관계를 걸면서 셋팅하고, 상태나 주문시간까지 셋팅되게 하도록 작성하는 것이 포인트
	 * 생성 지점이 변경되는 이 메서드만 신경쓰면 되므로 중요하다.
	 * @param member
	 * @param delivery
	 * @param orderItems
	 * @return
	 */
	public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
		Order order = new Order();
		order.setMember(member);
		order.setDelivery(delivery);

		for (OrderItem orderItem : orderItems) {
			order.addOrderItem(orderItem);
		}
		order.setStatus(OrderStatus.ORDER);
		order.setOrderDate(LocalDateTime.now());
		return order;
	}

	//==비지니스 로직==//
	public void cancel() {
		if(delivery.getStatus() == DeliveryStatus.COMP) {
			throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
		}

		this.setStatus(OrderStatus.CANCEL);
		for (OrderItem orderItem : orderItems) {
			 orderItem.cancel(); //orderItem에도 cancel을 해줘야 한다. 주문했을 때 상품을 2개 샀다면 2개 orderItem모두에 취소.
		}
	}

	//=조회 로직==//

	/**
	 * 전체 주문 가격 조회 <br>
	 * orderItem에서 주문 가격과 수량 속성이 있으므로, orderItem에서 전체 주문 금액 가져온다.
	 * @return
	 */
	public int getTotalPrice() {
		int totalPrice = 0;

		for (OrderItem orderItem : orderItems)
			totalPrice += orderItem.getTotalPrice();
		return totalPrice;
	}
}
