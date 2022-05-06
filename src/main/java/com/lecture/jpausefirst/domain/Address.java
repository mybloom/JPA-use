package com.lecture.jpausefirst.domain;

import javax.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {

	private String city;
	private String street;
	private String zipcode;

}
