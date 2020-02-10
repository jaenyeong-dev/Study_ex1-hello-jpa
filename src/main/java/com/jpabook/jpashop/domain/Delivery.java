package com.jpabook.jpashop.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
public class Delivery extends BaseEntity {

	@Id
	@GeneratedValue
	private Long id;

	private String city;
	private String street;
	private String zipcode;

	private DeliveryStatus status;

//	@OneToOne(mappedBy = "delivery")
	@OneToOne(mappedBy = "delivery", fetch = LAZY) // 지연 로딩으로 변경 (FetchType을 static import)
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
