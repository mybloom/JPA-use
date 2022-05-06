package com.lecture.jpausefirst.domain.item;


import com.lecture.jpausefirst.domain.Category;
import com.lecture.jpausefirst.exception.NotEnoughStockException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import lombok.Getter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //상속관계 테이블 전략 선택
@DiscriminatorColumn
@Getter
public abstract class Item {

	@Id
	@GeneratedValue
	@Column(name = "item_id")
	private Long id;

	private String name;

	private int price;

	private int stockQuantity;

	@ManyToMany(mappedBy = "items")
	private List<Category> categories = new ArrayList<>();

	//==비즈니스 로직==//
	//`데이터를 가지고 있는 쪽`에서 비지니스 메서드가 있는 것이 응집력이 좋다.

	/**
	 * 재고 증가
	 * @param quantity
	 */
	public void addStock(int quantity) {
		this.stockQuantity += quantity;
	}

	/**
	 * 재고 감소
	 */
	public void removeStock(int quantity) {
		int restStock = this.stockQuantity - quantity;

		if(restStock < 0 ){
			throw new NotEnoughStockException("need more stock");
		}

		this.stockQuantity = restStock;
	}
}
