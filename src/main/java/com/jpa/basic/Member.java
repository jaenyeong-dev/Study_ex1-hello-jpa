package com.jpa.basic;

import javax.persistence.*;

@Entity
@SequenceGenerator(name = "member_seq_generator", sequenceName = "member_seq", initialValue = 1, allocationSize = 50)
//@TableGenerator(name = "MEMBER_SEQ_GENERATOR", table = "MY_SEQUENCES", pkColumnValue = "MEMBER_SEQ", allocationSize = 1)
public class Member {
//	@Id
//	private Long id;
//	private String name;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_generator")
//	@GeneratedValue(strategy = GenerationType.TABLE, generator = "MEMBER_SEQ_GENERATOR")
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//	@Column(name = "name", nullable = false)
	@Column(name = "USER_NAME", nullable = false)
	private String userName;

	// 패러다임 불일치
//	@Column(name = "TEAM_ID")
//	private Long teamId;

	@ManyToOne
//	@ManyToOne(fetch = FetchType.LAZY) 지연 로딩 전략
	@JoinColumn(name = "TEAM_ID")
	private Team team;

//	private int age;
//
//	@Enumerated(EnumType.STRING)
//	private RoleType roleType;
//
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date createdDate;
//
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date lastModifiedDate;
//
//	@Lob
//	private String description;
//
//	// DB에서 사용하지 않을때
//	@Transient
//	private int temp;

	public Member() {
	}

//	public Member(Long id, String userName) {
//		this.id = id;
//		this.userName = userName;
//	}

//	public Long getId() {
//		return id;
//	}
//
//	public Member setId(Long id) {
//		this.id = id;
//		return this;
//	}

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

	public Member setUserName(String name) {
		this.userName = name;
		return this;
	}

//	public Long getTeamId() {
//		return teamId;
//	}
//
//	public Member setTeamId(Long teamId) {
//		this.teamId = teamId;
//		return this;
//	}

	public Team getTeam() {
		return team;
	}

	public Member setTeam(Team team) {
		this.team = team;
		return this;
	}

	// Team addMember() 메소드로 대체
//	public Member changeTeam(Team team) {
//		this.team = team;
//		this.team.getMembers().add(this);
//		return this;
//	}
}
