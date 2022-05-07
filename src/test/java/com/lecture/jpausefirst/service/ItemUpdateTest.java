package com.lecture.jpausefirst.service;

import com.lecture.jpausefirst.domain.item.Book;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ItemUpdateTest {

	@Autowired
	EntityManager entityManager;

	@Test
	void updateTest() {
		//given
		Book book = entityManager.find(Book.class, 1L);

		book.setName("이름변경");

		//변경값지 == dirty check
		//TX commit



	}
}