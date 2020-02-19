package com.jpql.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

	private String city;
	private String street;
	private String zipCode;

	public String getCity() {
		return city;
	}

	public Address setCity(String city) {
		this.city = city;
		return this;
	}

	public String getStreet() {
		return street;
	}

	public Address setStreet(String street) {
		this.street = street;
		return this;
	}

	public String getZipCode() {
		return zipCode;
	}

	public Address setZipCode(String zipCode) {
		this.zipCode = zipCode;
		return this;
	}
}
