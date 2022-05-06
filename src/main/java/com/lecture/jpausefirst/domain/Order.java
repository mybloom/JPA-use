package com.lecture.jpausefirst.domain;

import static javax.persistence.FetchType.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
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

	private OrderStatus orderStatus;

	public Order() {
	}
}
