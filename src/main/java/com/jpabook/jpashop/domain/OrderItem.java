package com.jpabook.jpashop.domain;

import javax.persistence.*;

@Entity
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ORDER_ITEM_ID")
	private Long id;

	@Column(name = "ORDER_ID")
	private Long orderId;

	@Column(name = "ITEM_ID")
	private Long itemId;

	private int orderPrice;
	private int count;

	public Long getId() {
		return id;
	}

	public OrderItem setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getOrderId() {
		return orderId;
	}

	public OrderItem setOrderId(Long orderId) {
		this.orderId = orderId;
		return this;
	}

	public Long getItemId() {
		return itemId;
	}

	public OrderItem setItemId(Long itemId) {
		this.itemId = itemId;
		return this;
	}

	public int getOrderPrice() {
		return orderPrice;
	}

	public OrderItem setOrderPrice(int orderPrice) {
		this.orderPrice = orderPrice;
		return this;
	}

	public int getCount() {
		return count;
	}

	public OrderItem setCount(int count) {
		this.count = count;
		return this;
	}
}
