package com.jpa.basic;

import com.jpa.basic.InheritanceMapping.Movie;
import com.jpa.basic.entity.Member;
import com.jpa.basic.entity.Team;

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

			mappedSuperclassExample(em);

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

	private static void mappedSuperclassExample(EntityManager em) {
		Member member =new Member();
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
