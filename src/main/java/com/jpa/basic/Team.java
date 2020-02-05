package com.jpa.basic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Team {

	@Id
	@GeneratedValue
	@Column
	private Long id;

	private String name;

	public Long getId() {
		return id;
	}

	public Team setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Team setName(String name) {
		this.name = name;
		return this;
	}
}
