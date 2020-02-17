package com.jpabook.jpashop.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Address {

	@Column(length = 10)
	private String city;
	@Column(length = 20)
	private String street;
	@Column(length = 5)
	private String zipCode;

	public Address() {
	}

	public String getCity() {
		return city;
	}

	public String getStreet() {
		return street;
	}

	public String getZipCode() {
		return zipCode;
	}

	/**
	 * equals(), hashCode() 생성시 getter를 사용하는 메소드를 생성할 것 (프록시일때 계산을 위해서)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Address address = (Address) o;
		return Objects.equals(getCity(), address.getCity()) &&
				Objects.equals(getStreet(), address.getStreet()) &&
				Objects.equals(getZipCode(), address.getZipCode());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCity(), getStreet(), getZipCode());
	}
}
