package com.jpa.basic.entity;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Address {
	private String city;
	private String street;
	private String zipCode;

	public Address() {
	}

	public Address(String city, String street, String zipCode) {
		this.city = city;
		this.street = street;
		this.zipCode = zipCode;
	}

	/**
	 * 불변 객체를 위해 각 필드 Setter 제거
	 * 내부적 사용시 private Setter를 생성하여 사용할 수도 있음
	 */

	public String getCity() {
		return city;
	}

//	public Address setCity(String city) {
//		this.city = city;
//		return this;
//	}

	public String getStreet() {
		return street;
	}

//	public Address setStreet(String street) {
//		this.street = street;
//		return this;
//	}

	public String getZipCode() {
		return zipCode;
	}

//	public Address setZipCode(String zipcode) {
//		this.zipCode = zipcode;
//		return this;
//	}

	/**
	 * 값 컬렉션 삭제를 위하여 equals(), hashCode() 메소드 오버라이딩
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Address address = (Address) o;
		return Objects.equals(city, address.city) &&
				Objects.equals(street, address.street) &&
				Objects.equals(zipCode, address.zipCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(city, street, zipCode);
	}
}
