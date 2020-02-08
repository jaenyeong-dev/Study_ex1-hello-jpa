package com.jpa.basic.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	// 다대다
//	@ManyToMany(mappedBy = "products")
//	private List<Member> members = new ArrayList<>();

	// 다대다에서 매핑 엔티티 생성한 경우
	@OneToMany(mappedBy = "product")
	private List<MemberProduct> memberProducts = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public Product setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Product setName(String name) {
		this.name = name;
		return this;
	}
}
