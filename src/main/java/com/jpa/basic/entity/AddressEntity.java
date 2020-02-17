package com.jpa.basic.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ADDRESS") // 빼먹으면 매핑이 제대로 안됨
public class AddressEntity {

	@Id
	@GeneratedValue
	private Long id;

	private Address address;

	public AddressEntity() {
	}

	public AddressEntity(String city, String street, String zipCode) {
		this.address = new Address(city, street, zipCode);
	}

	public Long getId() {
		return id;
	}

	public AddressEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public Address getAddress() {
		return address;
	}

	public AddressEntity setAddress(Address address) {
		this.address = address;
		return this;
	}
}
