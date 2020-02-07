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
//	@OneToMany(mappedBy = "team") // 일대다 단방향 참조일 때 mappedBy 제거
	@OneToMany
	@JoinColumn(name = "TEAM_ID") // 일대다 단방향 참조일 때 추가 (@JoinColumn을 사용하지 않으면 중간에 조인 테이블이 생김)
	private List<Member> members = new ArrayList<>();

	// 일대다 단방향 참조일 때 주석
//	public void addMember(Member member) {
//		member.setTeam(this);
//		members.add(member);
//	}

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
