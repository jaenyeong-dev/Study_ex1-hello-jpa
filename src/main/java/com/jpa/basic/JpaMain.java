package com.jpa.basic;

import com.jpa.basic.InheritanceMapping.Movie;
import com.jpa.basic.entity.*;
import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {

	public static void main(String[] args) {
		// Name은 persistence.xml에 persistence-unit 태그의 이름 속성값을 줌
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpaBasic");

		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		// execute code
		try {
//			basicExample(em);

//			jpqlExample(em);

//			cacheExample(em);

//			paradiamExample(em);

//			associateExample(em);

//			oneToManyExample(em);

//			inheritanceMappingExample(em);

//			mappedSuperclassExample(em);

//			proxyExample(em, emf);

//			lazyLoadingExample(em);

//			transitivePersistenceExample(em);

			embeddedExample(em);

			// 커밋 시점에 insert
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {

			em.close();
		}
		emf.close();

	}

	private static void embeddedExample(EntityManager em) {
		Member member = new Member();
		member.setUserName("Hello");
		member.setHomeAddress(new Address("city", "street", "123456"));
		member.setWorkPeriod(new Period());

		em.persist(member);
	}

	private static void transitivePersistenceExample(EntityManager em) {
		Child child1 = new Child();
		Child child2 = new Child();

		Parent parent = new Parent();
		parent.addChild(child1);
		parent.addChild(child2);

		em.persist(parent);
		// 영속성 전이 속성 사용시 아래 메소드 호출 안해도 됨
//		em.persist(child1);
//		em.persist(child2);

		em.flush();
		em.clear();

		Parent findParent = em.find(Parent.class, parent.getId());
		// 고아 객체 제거. orphanRemoval = true 테스트
		findParent.getChildList().remove(0);

		// cascade = CascadeType.ALL 테스트
//		em.remove(findParent);
	}

	private static void lazyLoadingExample(EntityManager em) {
		Team team1 = new Team();
		team1.setName("team1");
		em.persist(team1);

		Team teamB = new Team();
		teamB.setName("teamB");
		em.persist(teamB);

		Member member1 = new Member();
		member1.setUserName("member");
		member1.setTeam(team1);
		em.persist(member1);

		Member member2 = new Member();
		member2.setUserName("member2");
		member2.setTeam(teamB);
		em.persist(member2);

		em.flush();
		em.clear();

		// 지연로딩, 즉시로딩
//		Member findMember = em.find(Member.class, member.getId());
//		System.out.println("member getTeam: " + findMember.getTeam().getClass());
//
//		System.out.println("-----------------------------");
//		findMember.getTeam().getName(); // 지연 로딩시 이 시점에 초기화
//		System.out.println("-----------------------------");

		// JPQL 문제 > 먼저 SQL로 번역되어 member를 디비에서 가져옴 > team이 즉시 로딩인것을 확인 후 team을 바로(별도로) 가져옴
		// SQL : select * from member;
		// + SQL : select * from team where team_id = ***;
//		List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
		// JPQL에서 N+1 문제
		// fetch join으로 해결
		List<Member> members = em.createQuery("select m from Member m join fetch m.team", Member.class).getResultList();

	}

	private static void proxyExample(EntityManager em, EntityManagerFactory emf) {
//		Member findMember = em.find(Member.class, 1L);

//		printMember(findMember);
//		printMemberAndTeam(findMember);

//		Member member = new Member();
//		member.setUserName("Hello");
//
//		em.persist(member);
//		em.flush();
//		em.clear();
//
////    Member findMember = em.find(Member.class, member.getId()); // select 쿼리가 바로 실행
//		Member findMember = em.getReference(Member.class, member.getId()); // 필요 할때 select 쿼리가 실행됨
//
//		System.out.println("Before findMember class : " + findMember.getClass());
//		System.out.println("findMember ID : " + findMember.getId());
//		System.out.println("findMember userName : " + findMember.getUserName());
//		System.out.println("After findMember class : " + findMember.getClass());

		Member member1 = new Member();
		member1.setUserName("mem1");
		em.persist(member1);

		Member member2 = new Member();
		member2.setUserName("mem2");
		em.persist(member2);

		em.flush();
		em.clear();

		Member m1 = em.find(Member.class, member1.getId());
		Member m2 = em.getReference(Member.class, member2.getId());

		System.out.println("m1 == m2 " + (m1.getClass() == m2.getClass()));
		System.out.println("m1 instanceOf Member " + (m1 instanceof Member));
		System.out.println("m2 instanceOf Member " + (m2 instanceof Member));

		// 영속성 컨텍스트에 찾는 엔티티가 이미 존재하는 경우 em.getReference()를 호출해도 실제 엔티티 반환
		Member member3 = new Member();
		member3.setUserName("mem3");
		em.persist(member3);

		em.flush();
		em.clear();

		Member m3 = em.find(Member.class, member3.getId());
		System.out.println("m3 = " + m3.getClass());
		Member m3Proxy = em.getReference(Member.class, member3.getId());
		System.out.println("m3Proxy = " + m3Proxy.getClass());

		// 프록시 객체를 먼저 생성하게 되면 em.find() 메소드를 호출하여도 프록시 객체 사용
		Member member4 = new Member();
		member4.setUserName("mem4");
		em.persist(member4);

		em.flush();
		em.clear();

		Member m4Ref = em.getReference(Member.class, member4.getId());
		System.out.println("m4Ref: " + m4Ref.getClass()); // proxy

		Member m4Find = em.find(Member.class, member4.getId());
		System.out.println("m4Find: " + m4Find.getClass()); // proxy

		System.out.println("m4Ref == m4Find " + (m4Ref.getClass() == m4Find.getClass()));

		// 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, 프록시를 초기화하면 문제 발생
		Member member5 = new Member();
		member5.setUserName("mem5");
		em.persist(member5);

		em.flush();
		em.clear();

		Member m5Ref = em.getReference(Member.class, member5.getId());
		System.out.println("m5Ref: " + m5Ref.getClass()); // proxy

//		em.detach(m5Ref);
//		em.close();
//		em.clear();

		// 위 명령 호출 시 [LazyInitializationException] could not initialize proxy 이셉션 발생
		m5Ref.getUserName();

		// 프록시 확인
		Member member6 = new Member();
		member6.setUserName("mem6");
		em.persist(member6);

		em.flush();
		em.clear();

		Member m6Ref = em.getReference(Member.class, member6.getId());
		System.out.println("m6Ref: " + m6Ref.getClass()); // proxy

//		m6Ref.getUserName(); // 초기화 테스트
//		System.out.println("isLoaded: " + emf.getPersistenceUnitUtil().isLoaded(m6Ref)); // 초기화 여부 확인

		Hibernate.initialize(m6Ref);  // 강제 초기화
	}

//	private static void printMember(Member findMember) {
//		String userName = findMember.getUserName();
//		System.out.println("userName : " + userName);
//	}

//	private static void printMemberAndTeam(Member findMember) {
//		String userName = findMember.getUserName();
//		System.out.println("userName : " + userName);
//
//		Team team = findMember.getTeam();
//		System.out.println("Team : " + team);
//	}

	private static void mappedSuperclassExample(EntityManager em) {
		Member member = new Member();
		member.setUserName("user1");
		member.setCreatedBy("Kim");
		member.setCreatedDate(LocalDateTime.now());

		em.persist(member);

		em.flush();
		em.clear();

	}

	private static void inheritanceMappingExample(EntityManager em) {
		Movie movie = new Movie();
		movie.setDirector("A");
		movie.setActor("B");
		movie.setName("Wind");
		movie.setPrice(10000);

		em.persist(movie);

		em.flush();
		em.clear();

		Movie findMovie = em.find(Movie.class, movie.getId());
		System.out.println("find Movie " + findMovie);
	}

	private static void oneToManyExample(EntityManager em) {
		// Team 테이블의 값을 삽입, 변경하는데, Member 테이블 값이 변경되면 운영상 어려움
		Member member = new Member();
		member.setUserName("member1");

		em.persist(member);

		Team team = new Team();
		team.setName("teamA");
		// Team 테이블에 있지 않고 Member 테이블에 값을 변경 해줘야 함
		team.getMembers().add(member);

		em.persist(team);
	}

	private static void associateExample(EntityManager em) {
		// 연관관계 매핑
		Team team = new Team();
		team.setName("TeamA");
		em.persist(team);

		Member member = new Member();
		member.setUserName("member1");
		// 아래 team.addMember(member); 메소드로 대체 (한 쪽에만 있는 것이 좋음)
//			member.changeTeam(team); // setter 대신 작성한 메소드를 사용
		em.persist(member);

		// 역방향만 연관관계 설정
		// 1. em.flush(), em.clear()를 하지 않을경우 직접 호출하여 1차 캐시에 저장해야함
		// 2. Test Case를 위하여
		// 연관관계 편의 메소를 생성하여 아래 로직을 메소드 안에서 실행
//			team.getMembers().add(member);

		// 일대다 단방향일 때 주석
//			team.addMember(member); // setter 대신 작성한 메소드를 사용

		// 쿼리를 직접 실행하여 가져오고 싶을 때
//			em.flush();
//			em.clear();

//			Member findMember = em.find(Member.class, member.getId());
//			Team findTeam = findMember.getTeam();
//			System.out.println("find team name " + findTeam.getName());

		Team findTeam = em.find(Team.class, team.getId());

		List<Member> teamMembers = findTeam.getMembers();

		for (Member m : teamMembers) {
			System.out.println("member = " + m.getUserName());
		}

		// Team을 변경시 (100번 팀이 있다는 가정하에)
//			Team newTeam = em.find(Team.class, 100L);
//			findMember.setTeam(newTeam);
	}

	private static void paradiamExample(EntityManager em) {
		// 패러다임 불일치로 인해 데이터를 가져오기 번거로움
//		Team team = new Team();
//		team.setName("TeamA");
//		em.persist(team);
//
//		Member member = new Member();
//		member.setUserName("member1");
//		member.setTeamId(team.getId());
//		em.persist(member);
//
//		Member findMember = em.find(Member.class, member.getId());
//		Long findTeamId = findMember.getTeamId();
//		Team findTeam = em.find(Team.class, findTeamId);
	}

	private static void cacheExample(EntityManager em) {
//          Member memberA = new Member();
////          member.setId("ID_A");
//			memberA.setUserName("A");
//
//			Member memberB = new Member();
//			memberB.setUserName("B");
//
//			Member memberC = new Member();
//			memberC.setUserName("C");
//
//			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//
//          em.persist(memberA);  // 1, 51 - member.setId(1L); 인경우
//			em.persist(memberB);  // memory
//			em.persist(memberC);  // memory
//
//			System.out.println("member A " + memberA.getId());
//			System.out.println("member B " + memberB.getId());
//			System.out.println("member C " + memberC.getId());
	}

	private static void jpqlExample(EntityManager em) {
		// JPQL
//		List<Member> result = em.createQuery("select m from Member as m", Member.class)
//				.setFirstResult(5)
//				.setMaxResults(8)
//				.getResultList();
//
//		for (Member mem : result) {
//			System.out.println("member.name : " + mem.getUserName());
//		}
	}

	private static void basicExample(EntityManager em) {
		// insert (비영속)
//		Member member = new Member();
//		member.setId(1L);
//		member.setName("helloA");
//		member.setId(2L);
//		member.setName("helloB");
//
//		Member member = new Member(101L, "TEST");
//		em.persist(member);
//
//		// 영속
//		System.out.println(">> Before <<");
//		em.persist(member);
//		System.out.println(">> After <<");
//
//		// 준영속
//		em.detach(member);
//
//		// update
//		Member findMember1 = em.find(Member.class, 1L);
//		Member findMember2 = em.find(Member.class, 1L);
//		System.out.println("findMember id: " + findMember1.getId());
//		System.out.println("findMember name: " + findMember1.getUserName());
//		findMember1.setName("Hello JPA");
//
//		// 영속 엔티티 동일성 보장
//		System.out.println("result = " + (findMember1 == findMember2));
//
//		// delete
//		em.remove(findMember);
	}
}
