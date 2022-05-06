package com.lecture.jpausefirst.repository;

import com.lecture.jpausefirst.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {

	private Setter memberName; //회원명
	private OrderStatus orderStatus; //주문상태
}
