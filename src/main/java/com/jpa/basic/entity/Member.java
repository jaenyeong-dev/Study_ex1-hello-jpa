package com.jpa.basic.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

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

	// 컬렴 어노테이션 이름 속성 변경
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

	// 기간 Period
//	private LocalDateTime startDate;
//	private LocalDateTime endDate;
	@Embedded
	private Period workPeriod;

	// 주소 Address
//	private String city;
//	private String street;
//	private String zipcode;
	@Embedded
	private Address homeAddress;

	/**
	 * 값 타입 컬렉션
	 * private Set<String> favoriteFoods;
	 * private List<Address> addressHistory;
	 */

	@ElementCollection // default fetch lazy
	@CollectionTable(name = "FAVORITE_FOOD", joinColumns = @JoinColumn(name = "MEMBER_ID"))
	@Column(name = "FOOD_NAME") // String 하나라서 가능 (예외적으로 가능)
	private Set<String> favoriteFoods = new HashSet<>();

////  @OrderColumn(name = "address_history_order") // 컬렉션 순서 값 컬럼을 사용하여 처리 가능하나 가급적 사용하지 않는 것을 권장
//	@ElementCollection // default fetch lazy
//	@CollectionTable(name = "ADDRESS", joinColumns = @JoinColumn(name = "MEMBER_ID"))
//	private List<Address> addressHistory = new ArrayList<>();

	// 위 방식보다 아래 방식 사용 추천
	@OneToMany(cascade = ALL, orphanRemoval = true)
	@JoinColumn(name = "MEMBER_ID")
	private List<AddressEntity> addressHistory = new ArrayList<>();

	// 주소 객체 중복
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "city", column = @Column(name = "WORK_CITY")),
			@AttributeOverride(name = "street", column = @Column(name = "WORK_STREET")),
			@AttributeOverride(name = "zipCode", column = @Column(name = "WORK_ZIPCODE"))
	})
	private Address workAddress;

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

	public Period getWorkPeriod() {
		return workPeriod;
	}

	public Member setWorkPeriod(Period workPeriod) {
		this.workPeriod = workPeriod;
		return this;
	}

	public Address getHomeAddress() {
		return homeAddress;
	}

	public Member setHomeAddress(Address homeAddress) {
		this.homeAddress = homeAddress;
		return this;
	}

	public Set<String> getFavoriteFoods() {
		return favoriteFoods;
	}

	public Member setFavoriteFoods(Set<String> favoriteFoods) {
		this.favoriteFoods = favoriteFoods;
		return this;
	}

//	public List<Address> getAddressHistory() {
//		return addressHistory;
//	}
//
//	public Member setAddressHistory(List<Address> addressHistory) {
//		this.addressHistory = addressHistory;
//		return this;
//	}

	public List<AddressEntity> getAddressHistory() {
		return addressHistory;
	}

	public Member setAddressHistory(List<AddressEntity> addressHistory) {
		this.addressHistory = addressHistory;
		return this;
	}

	public Address getWorkAddress() {
		return workAddress;
	}

	public Member setWorkAddress(Address workAddress) {
		this.workAddress = workAddress;
		return this;
	}
}
