package com.jpa.basic.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	// 영속성 전이, 고아 객체 제거
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
//	@OneToMany(mappedBy = "parent", orphanRemoval = true)
//	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL) // 이 경우에도 childList 다지움
	private List<Child> childList = new ArrayList<>();

	public void addChild(Child child) {
		childList.add(child);
		child.setParent(this);
	}

	public Long getId() {
		return id;
	}

	public Parent setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Parent setName(String name) {
		this.name = name;
		return this;
	}

	public List<Child> getChildList() {
		return childList;
	}

	public Parent setChildList(List<Child> childList) {
		this.childList = childList;
		return this;
	}
}
