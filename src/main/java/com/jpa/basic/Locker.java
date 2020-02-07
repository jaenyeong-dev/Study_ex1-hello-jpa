package com.jpa.basic;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Locker {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@OneToOne(mappedBy = "locker")
	private Member member;

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

	public Member getMember() {
		return member;
	}

	public Locker setMember(Member member) {
		this.member = member;
		return this;
	}
}
