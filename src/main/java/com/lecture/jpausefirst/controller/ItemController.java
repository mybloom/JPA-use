package com.lecture.jpausefirst.controller;

import com.lecture.jpausefirst.domain.item.Book;
import com.lecture.jpausefirst.domain.item.Item;
import com.lecture.jpausefirst.service.ItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@GetMapping("items/new")
	private String createForm(Model model) {
		model.addAttribute("form", new BookForm());
		return "/items/createItemForm";
	}

	@PostMapping("items/new")
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

	@GetMapping("items")
	public String list(Model model) {
		List<Item> items = itemService.findItems();
		model.addAttribute("items", items);
		return "/items/itemList";
	}

	@GetMapping("items/{itemId}/edit")
	private String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
		Book item = (Book) itemService.findOne(itemId);
		BookForm form = new BookForm();
		//라이브러리 있다 : modelMapper등
		form.setId(item.getId());
		form.setName(item.getName());
		form.setPrice(item.getPrice());
		form.setStockQuantity(item.getStockQuantity());
		form.setAuthor(item.getAuthor());
		form.setIsbn(item.getIsbn());

		model.addAttribute("form", form);
		return "/items/updateItemForm";
	}

	@PostMapping("items/{itemId}/edit")
	public String updateItem(@ModelAttribute("form") BookForm form) { //ModelAttribute : form에서 넘겨준 form을 객체로 사용할 수 있다.
		Book book = new Book();
		book.setId(form.getId());
		book.setName(form.getName());
		book.setPrice(form.getPrice());
		book.setStockQuantity(form.getStockQuantity());
		book.setAuthor(form.getAuthor());
		book.setIsbn(form.getIsbn());

		itemService.saveItem(book);
		return "redirect:/items";
	}
}
