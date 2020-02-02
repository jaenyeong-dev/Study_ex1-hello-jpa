package com.jpa.basic;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Member {
	@Id
	private Long id;
	private String name;

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
}
