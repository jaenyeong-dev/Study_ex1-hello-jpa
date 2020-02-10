package com.jpabook.jpashop.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
public class OrderItem extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ORDER_ITEM_ID")
	private Long id;

	// 필요 없어짐
//	@Column(name = "ORDER_ID")
//	private Long orderId;

//	@ManyToOne
	@ManyToOne(fetch = LAZY) // 지연 로딩으로 변경 (FetchType을 static import)
	@JoinColumn(name = "ORDER_ID")
	private Order order;

	// 필요 없어짐
//	@Column(name = "ITEM_ID")
//	private Long itemId;

//	@ManyToOne
	@ManyToOne(fetch = LAZY) // 지연 로딩으로 변경 (FetchType을 static import)
	@JoinColumn(name = "ITEM_ID")
	private Item item;

	private int orderPrice;
	private int count;

	public Long getId() {
		return id;
	}

	public OrderItem setId(Long id) {
		this.id = id;
		return this;
	}

	public Order getOrder() {
		return order;
	}

	public OrderItem setOrder(Order order) {
		this.order = order;
		return this;
	}

	public Item getItem() {
		return item;
	}

	public OrderItem setItem(Item item) {
		this.item = item;
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
