package com.lecture.jpausefirst.domain;

import static javax.persistence.FetchType.LAZY;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Delivery {

	@Id
	@GeneratedValue
	@Column(name = "delivery_id")
	private Long id;

	@OneToOne(mappedBy = "delivery", fetch = LAZY) //1:1의 거울로 설정
	private Order order;

	@Embedded
	private Address address;

	@Enumerated(EnumType.STRING) //코드 값이 아니라 문자값을 넣는다. 코드는 순서에 따라 의미하는 값이 달라질 수 있다.
	private DeliveryStatus status;
}
