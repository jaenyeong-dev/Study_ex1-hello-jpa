package com.jpabook.jpashop.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Delivery {

	@Id
	@GeneratedValue
	private Long id;

	private String city;
	private String street;
	private String zipcode;

	private DeliveryStatus status;

	@OneToOne(mappedBy = "delivery")
	private Order order;

	public Long getId() {
		return id;
	}

	public Delivery setId(Long id) {
		this.id = id;
		return this;
	}

	public String getCity() {
		return city;
	}

	public Delivery setCity(String city) {
		this.city = city;
		return this;
	}

	public String getStreet() {
		return street;
	}

	public Delivery setStreet(String street) {
		this.street = street;
		return this;
	}

	public String getZipcode() {
		return zipcode;
	}

	public Delivery setZipcode(String zipcode) {
		this.zipcode = zipcode;
		return this;
	}

	public DeliveryStatus getStatus() {
		return status;
	}

	public Delivery setStatus(DeliveryStatus status) {
		this.status = status;
		return this;
	}

	public Order getOrder() {
		return order;
	}

	public Delivery setOrder(Order order) {
		this.order = order;
		return this;
	}
}
