package com.jpa.basic.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@SequenceGenerator(name = "member_seq_generator", sequenceName = "member_seq", initialValue = 1, allocationSize = 50)
//@TableGenerator(name = "MEMBER_SEQ_GENERATOR", table = "MY_SEQUENCES", pkColumnValue = "MEMBER_SEQ", allocationSize = 1)
public class Member extends BaseEntity {
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

	// 일대다 단방향일 때 주석처리
//	@ManyToOne
////	@ManyToOne(fetch = FetchType.LAZY) 지연 로딩 전략
//	@JoinColumn(name = "TEAM_ID")
//	private Team team;

	// 일대다 양방향
//	@ManyToOne
//	@JoinColumn(name = "TEAM_ID", insertable = false, updatable = false) // 읽기 전용
	// 지연로딩
//	@ManyToOne(fetch = FetchType.LAZY)
	// 즉시 로딩
	@ManyToOne(fetch = FetchType.EAGER)
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

	// 프록시 예제를 위한 주석
	// Locker 1:1 매핑
//	@OneToOne
//	@JoinColumn(name = "LOCKER_ID")
//	private Locker locker;

	// 다대다
//	@ManyToMany
//	@JoinTable(name = "MEMBER_PRODUCT")
//	private List<Product> products = new ArrayList<>();

	// 프록시 예제를 위한 주석
	// 다대다에서 매핑 엔티티 생성한 경우
//	@OneToMany(mappedBy = "member")
//	private List<MemberProduct> memberProducts = new ArrayList<>();

//	private String createdBy;
//	private LocalDateTime createdDate;
//	private String lastModifiedBy;
//	private LocalDateTime lastModifiedDate;

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
