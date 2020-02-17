package com.jpabook.jpashop.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MEMBER_ID")
	private Long id;
	private String name;

	// 값 타입 객체 추가
//	private String city;
//	private String street;
//	private String zipCode;
	@Embedded // Address 클래스에 @Embeddable 어노테이션 태깅하면 태깅 안해도 되나 그냥 붙임
	private Address address;

	// 여기서 Member 안에 Order가 있는 건 일반적으로 좋지 않은 설계이나 여기서는 그냥 진행
	// 관례로 ArrayList 초기화 (Null 방지)
	// mappedBy는 orders의 Member 필드 변수명 member
	@OneToMany(mappedBy = "member")
	private List<Order> orders = new ArrayList<>();

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

//	public String getCity() {
//		return city;
//	}
//
//	public Member setCity(String city) {
//		this.city = city;
//		return this;
//	}
//
//	public String getStreet() {
//		return street;
//	}
//
//	public Member setStreet(String street) {
//		this.street = street;
//		return this;
//	}
//
//	public String getZipCode() {
//		return zipCode;
//	}
//
//	public Member setZipCode(String zipCode) {
//		this.zipCode = zipCode;
//		return this;
//	}

	public Address getAddress() {
		return address;
	}

	public Member setAddress(Address address) {
		this.address = address;
		return this;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public Member setOrders(List<Order> orders) {
		this.orders = orders;
		return this;
	}
}
