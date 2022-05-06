package com.lecture.jpausefirst.domain;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA스펙상 엔티티를 객체로 생성하기 위한 기본생성자 제공
public class Address {

	private String city;
	private String street;
	private String zipcode;

}
