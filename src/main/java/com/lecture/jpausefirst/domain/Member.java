package com.lecture.jpausefirst.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

	@Id
	@GeneratedValue
	@Column(name = "member_id")
	private Long id;

	@NotEmpty
	private String name;

	@Embedded
	private Address address;

	@OneToMany(mappedBy = "member") //`order 객체에 있는 member 필드에 의해 매핑된 것
	private List<Order> orders = new ArrayList<>();

}
