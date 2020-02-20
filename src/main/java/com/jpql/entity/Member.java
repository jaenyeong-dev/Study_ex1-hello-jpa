package com.jpql.entity;

import javax.persistence.*;

@Entity
//@NamedQuery(
//		name = "Member.findByUserName",  // 쿼리명 관례는 엔티티.쿼리명
//		query = "SELECT m FROM Member m WHERE m.userName = :userName"  // 실행 시점에 쿼리 무결성 체크
//) // META-INF/ormMember.xml 파일에 설정을 해도 사용 가능
public class Member {

	@Id
	@GeneratedValue
	private Long id;

	private String userName;

	private int age;

	@ManyToOne(fetch = FetchType.LAZY) // Lazy 설정하는 것을 권장
	@JoinColumn(name = "TEAM_ID")
	private Team team;

	@Enumerated(EnumType.STRING) // 주의! >> 기본값이 숫자(ORDINAL)이기 때문에 바꿔줄 것
	private MemberType type;

	public Member() {
	}

	public void changeTeam(Team team) {
		this.team = team;
		team.getMembers().add(this);
	}

	public Long getId() {
		return id;
	}

	public Member setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public Member setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	public int getAge() {
		return age;
	}

	public Member setAge(int age) {
		this.age = age;
		return this;
	}

	public Team getTeam() {
		return team;
	}

	public Member setTeam(Team team) {
		this.team = team;
		return this;
	}

	public MemberType getType() {
		return type;
	}

	public Member setType(MemberType type) {
		this.type = type;
		return this;
	}

	// Team 조심할 것 (무한루프)
	@Override
	public String toString() {
		return "Member{" +
				"id=" + id +
				", userName='" + userName + '\'' +
				", age=" + age +
				'}';
	}
}
