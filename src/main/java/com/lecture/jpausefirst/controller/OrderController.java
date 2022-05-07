package com.lecture.jpausefirst.controller;

import com.lecture.jpausefirst.domain.Member;
import com.lecture.jpausefirst.domain.Order;
import com.lecture.jpausefirst.domain.item.Item;
import com.lecture.jpausefirst.repository.OrderSearch;
import com.lecture.jpausefirst.service.ItemService;
import com.lecture.jpausefirst.service.MemberService;
import com.lecture.jpausefirst.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	private final MemberService memberService;
	private final ItemService itemService;

	@GetMapping("/order")
	public String createForm(Model model) {
		List<Member> members = memberService.findMembers();
		List<Item> items = itemService.findItems();

		model.addAttribute("members", members);
		model.addAttribute("items", items);

		return "order/orderForm";
	}

	@PostMapping("/order")
	public String order(@RequestParam("memberId") Long memberId,
		@RequestParam("itemId") Long itemId,
		@RequestParam("count") int count) {
		orderService.order(memberId, itemId, count);
		return "redirect:/orders"; //주문내역 목록
	}

	@GetMapping("/orders")
	public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
		List<Order> orders = orderService.findOrders(orderSearch); //service에서 단순 위임이면 controller에서 repository 직접 접근한다.
		model.addAttribute("orders", orders);

		return "order/orderList";
	}

	@PostMapping("/orders/{orderId}/cancel")
	public String orderCancel(@PathVariable("orderId") Long orderId) {
		orderService.cancelOrder(orderId);
		return "redirect:/orders";
	}
}
