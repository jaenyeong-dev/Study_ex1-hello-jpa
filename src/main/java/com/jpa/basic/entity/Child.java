package com.jpa.basic.entity;

import javax.persistence.*;

@Entity
public class Child {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Parent parent;

	public Long getId() {
		return id;
	}

	public Child setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Child setName(String name) {
		this.name = name;
		return this;
	}

	public Parent getParent() {
		return parent;
	}

	public Child setParent(Parent parent) {
		this.parent = parent;
		return this;
	}
}
