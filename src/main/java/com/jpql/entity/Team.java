package com.jpql.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

//	@BatchSize(size = 100) // FETCH JOIN을 사용하지 않고 Team을 SELECT 한 쿼리에서 페이징 처리시 태깅
	@OneToMany(mappedBy = "team")
//	@OrderColumn
	private List<Member> members = new ArrayList<>();

	public Team() {
	}

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

	public List<Member> getMembers() {
		return members;
	}

	public Team setMembers(List<Member> members) {
		this.members = members;
		return this;
	}
}
