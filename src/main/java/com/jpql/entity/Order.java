package com.jpql.entity;

import javax.persistence.*;

@Entity
@Table(name = "ORDERS")
public class Order {

	@Id
	@GeneratedValue
	private Long id;

	private int orderAmonut;

	@Embedded
	private Address address;

	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;

	public Long getId() {
		return id;
	}

	public Order setId(Long id) {
		this.id = id;
		return this;
	}

	public int getOrderAmonut() {
		return orderAmonut;
	}

	public Order setOrderAmonut(int orderAmonut) {
		this.orderAmonut = orderAmonut;
		return this;
	}

	public Address getAddress() {
		return address;
	}

	public Order setAddress(Address address) {
		this.address = address;
		return this;
	}

	public Product getProduct() {
		return product;
	}

	public Order setProduct(Product product) {
		this.product = product;
		return this;
	}
}
