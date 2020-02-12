package com.jpa.basic.entity;

import javax.persistence.Embeddable;

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

	public Address setZipCode(String zipcode) {
		this.zipCode = zipcode;
		return this;
	}
}
