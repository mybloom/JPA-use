package com.lecture.jpausefirst.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.lecture.jpausefirst.domain.Address;
import com.lecture.jpausefirst.domain.Member;
import com.lecture.jpausefirst.domain.Order;
import com.lecture.jpausefirst.domain.OrderStatus;
import com.lecture.jpausefirst.domain.item.Book;
import com.lecture.jpausefirst.repository.OrderRepository;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

	@Autowired
	EntityManager entityManager;
	@Autowired
	OrderService orderService;
	@Autowired
	OrderRepository orderRepository;

	@Test
	void 상품주문() {
		//given
		Member member = new Member();
		member.setName("회원1");
		member.setAddress(new Address("서울", "강남구", "123-123"));
		entityManager.persist(member);

		Book book = new Book();
		book.setName("JPA");
		book.setPrice(39000);
		book.setStockQuantity(10);
		entityManager.persist(book);

		int orderCount = 2;

		//when
		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

		//then
		Order getOrder = orderRepository.findOne(orderId);

		assertEquals(OrderStatus.ORDER, getOrder.getStatus());
		assertEquals(1, getOrder.getOrderItems().size());
		assertEquals(39000 * orderCount, getOrder.getTotalPrice());;
		assertEquals(8, book.getStockQuantity());
	}

	@Test
	void 주문취소() {
		//given

		//when

		//then

	}

	@Test
	void 상품주문_재고수량초과() {
		//given

		//when

		//then

	}
}