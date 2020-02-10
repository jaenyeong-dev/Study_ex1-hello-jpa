package com.jpabook.jpashop.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "ORDERS")
public class Order extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ORDER_ID")
	private Long id;

	// 필요 없어짐
//	@Column(name = "MEMBER_ID")
//	private Long memberId;

	// 연관관계 매핑
//	@ManyToOne
	// 지연 로딩으로 변경 (FetchType을 static import)
	// 영속성 전이 설정 (cascade static import)
	@ManyToOne(fetch = LAZY, cascade = ALL)
	@JoinColumn(name = "MEMBER_ID")
	private Member member;

	// Order 안에 OrderItem이 있는 경우는 일반적
	@OneToMany(mappedBy = "order", cascade = ALL)
	private List<OrderItem> orderItems = new ArrayList<>();

	private LocalDateTime orderDate;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

//	@OneToOne
	// 지연 로딩으로 변경 (FetchType static import)
	// 영속성 전이 설정 (cascade static import)
	@OneToOne(fetch = LAZY, cascade = ALL)
	@JoinColumn(name = "DELIVERY_ID")
	private Delivery delivery;

	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
		orderItem.setOrder(this);
	}

	public Long getId() {
		return id;
	}

	public Order setId(Long id) {
		this.id = id;
		return this;
	}

	public Member getMember() {
		return member;
	}

	public Order setMember(Member member) {
		this.member = member;
		return this;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public Order setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
		return this;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public Order setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
		return this;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public Order setStatus(OrderStatus status) {
		this.status = status;
		return this;
	}

}
