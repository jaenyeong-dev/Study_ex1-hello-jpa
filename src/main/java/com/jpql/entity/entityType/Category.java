package com.jpql.entity.entityType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Category extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "CATEGORY_ID")
	private Long id;

	private String name;

//	@ManyToOne
	@ManyToOne(fetch = LAZY) // 지연 로딩으로 변경 (FetchType을 static import)
	@JoinColumn(name = "PARENT_ID")
	private Category parent;

	@OneToMany(mappedBy = "parent")
	private List<Category> child = new ArrayList<>();

	@ManyToMany
	@JoinTable(name = "CATEGORY_ITEM",
			joinColumns = @JoinColumn(name = "CATEGORY_ID"),
			inverseJoinColumns = @JoinColumn(name = "ITEM_ID"))
	private List<Item> items = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public Category setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Category setName(String name) {
		this.name = name;
		return this;
	}

	public Category getParent() {
		return parent;
	}

	public Category setParent(Category parent) {
		this.parent = parent;
		return this;
	}

	public List<Category> getChild() {
		return child;
	}

	public Category setChild(List<Category> child) {
		this.child = child;
		return this;
	}

	public List<Item> getItems() {
		return items;
	}

	public Category setItems(List<Item> items) {
		this.items = items;
		return this;
	}
}
