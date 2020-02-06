package com.jpa.basic;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

	@Id
	@GeneratedValue
	@Column
	private Long id;

	private String name;
	// 초기화 해두는 것이 관례 (null 방지)
	// mappedBy는 매핑 변수명 (Member 클래스 안에 Team 필드 변수명)
	@OneToMany(mappedBy = "team")
	private List<Member> members = new ArrayList<>();

	public void addMember(Member member) {
		member.setTeam(this);
		members.add(member);
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
