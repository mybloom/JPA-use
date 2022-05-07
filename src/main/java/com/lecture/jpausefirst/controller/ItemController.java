package com.lecture.jpausefirst.controller;

import com.lecture.jpausefirst.domain.item.Book;
import com.lecture.jpausefirst.domain.item.Item;
import com.lecture.jpausefirst.service.ItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@GetMapping("/items/new")
	private String createForm(Model model) {
		model.addAttribute("form", new BookForm());
		return "items/createItemForm";
	}

	@PostMapping("/items/new")
	public String create(BookForm form) {
		Book book = new Book();
		//setter를 없애고 book.createBook() 의 메서드 호출하는 것으로 생성하는 것이 더 좋은 설계
		//편의상 빠른 개발을 위해 setter 사용.
		book.setName(form.getName());
		book.setPrice(form.getPrice());
		book.setStockQuantity(form.getStockQuantity());
		book.setAuthor(form.getAuthor());
		book.setIsbn(form.getIsbn());

		itemService.saveItem(book);
		return "redirect:/";
	}

	@GetMapping("/items")
	public String list(Model model) {
		List<Item> items = itemService.findItems();
		model.addAttribute("items", items);
		return "items/itemList";
	}

}
