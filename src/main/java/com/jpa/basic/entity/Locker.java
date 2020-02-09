package com.jpa.basic.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Locker {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	// 프록시 예제를 위한 주석
//	@OneToOne(mappedBy = "locker")
//	private Member member;

	public Long getId() {
		return id;
	}

	public Locker setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Locker setName(String name) {
		this.name = name;
		return this;
	}

//	public Member getMember() {
//		return member;
//	}
//
//	public Locker setMember(Member member) {
//		this.member = member;
//		return this;
//	}
}
