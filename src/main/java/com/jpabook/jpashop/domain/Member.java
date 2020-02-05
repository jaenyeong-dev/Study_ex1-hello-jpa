package com.jpabook.jpashop.domain;

import javax.persistence.*;

@Entity
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MEMBER_ID")
	private Long id;
	private String name;
	private String city;
	private String street;
	private String zipCode;

	public Long getId() {
		return id;
	}

	public Member setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Member setName(String name) {
		this.name = name;
		return this;
	}

	public String getCity() {
		return city;
	}

	public Member setCity(String city) {
		this.city = city;
		return this;
	}

	public String getStreet() {
		return street;
	}

	public Member setStreet(String street) {
		this.street = street;
		return this;
	}

	public String getZipCode() {
		return zipCode;
	}

	public Member setZipCode(String zipCode) {
		this.zipCode = zipCode;
		return this;
	}
}
