package com.jpql;

import com.jpql.DTO.MemberDTO;
import com.jpql.entity.Address;
import com.jpql.entity.Member;
import com.jpql.entity.MemberType;
import com.jpql.entity.Team;
import com.jpql.entity.entityType.Item;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

public class JpqlMain {

	public static void main(String[] args) {
		// Name은 persistence.xml에 persistence-unit 태그의 이름 속성값을 줌
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpql");

		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		// execute code
		try {

//			queryTypeExample(em);

//			getResultExample(em);

//			bindParameterExample(em);

//			projectionExample(em);

//			pagingExample(em);

//			joinExample(em);

//			typeExpressionExample(em);

//			caseExpressionExample(em);

//			functionExample(em);

//			pathExpressionExample(em);

//			fetchJoinExample(em);

//			useEntityDirectlyExample(em);

//			nameQueryExample(em);

			bulkExample(em);

			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {

			em.close();
		}
		emf.close();
	}

	private static void bulkExample(EntityManager em) {

		Member member1 = new Member();
		member1.setUserName("멤버1");
		member1.setAge(11);
		em.persist(member1);

		Member member2 = new Member();
		member2.setUserName("멤버2");
		member2.setAge(12);
		em.persist(member2);

		Member member3 = new Member();
		member3.setUserName("멤버3");
		member3.setAge(13);
		em.persist(member3);

		// 벌크연산
		// 수정, 삭제 다 가능
		// 실행 전 flush는 됨 (FLUSH는 자동 호출)
		String queryStr = "UPDATE Member m SET m.age = 20";
		int updateCnt = em.createQuery(queryStr).executeUpdate();
		System.out.println("UPDATE Count = " + updateCnt);

		// 영속성 컨텍스트는 갱신되지 않음
		System.out.println("Before member 1 = " + member1);
		System.out.println("Before member 2 = " + member2);
		System.out.println("Before member 3 = " + member3);

		// 기존 Member 인스턴스들을 사용하면 안됨
		// 벌크 연산 후 영속성 컨텍스트 초기화 후 다시 가져와야 함
		em.clear();

		Member findMember1 = em.find(Member.class, member1.getId());
		Member findMember2 = em.find(Member.class, member2.getId());
		Member findMember3 = em.find(Member.class, member3.getId());

		System.out.println("After member 1 = " + findMember1);
		System.out.println("After member 2 = " + findMember2);
		System.out.println("After member 3 = " + findMember3);
	}

	private static void nameQueryExample(EntityManager em) {

		Member member1 = new Member();
		member1.setUserName("멤버1");
		em.persist(member1);

		List<Member> memberList = em.createNamedQuery("Member.findByUserName", Member.class)
				.setParameter("userName", "멤버1")
				.getResultList();
		for (Member member : memberList) {
			System.out.println("member : " + member);
		}
	}

	private static void useEntityDirectlyExample(EntityManager em) {

		Team teamA = new Team();
		teamA.setName("teamA");
		em.persist(teamA);

		Member member1 = new Member();
		member1.setUserName("회원1");
		member1.setTeam(teamA);
		em.persist(member1);

		em.flush();
		em.clear();

		// 엔티티를 직접 사용할 때 (기본키)
		String useEntityPKDirectlyQuery = "SELECT m FROM Member m WHERE m = :member";
		Member findMember1 = em.createQuery(useEntityPKDirectlyQuery, Member.class)
				.setParameter("member", member1)
				.getSingleResult();

		System.out.println("findMember1 = " + findMember1);

		em.flush();
		em.clear();

		// 엔티티의 ID를 사용할 때 (기본키)
		String useEntityPKQuery = "SELECT m FROM Member m WHERE m.id = :memberId";
		Member findMember2 = em.createQuery(useEntityPKQuery, Member.class)
				.setParameter("memberId", member1.getId())
				.getSingleResult();
		System.out.println("findMember2 = " + findMember2);

		em.flush();
		em.clear();

		// 엔티티를 직접 사용할 때 (외래키)
		String useEntityFKDirectlyQuery = "SELECT m FROM Member m WHERE m.team = :team";
		Member findMember3 = em.createQuery(useEntityFKDirectlyQuery, Member.class)
				.setParameter("team", teamA)
				.getSingleResult();
		System.out.println("findMember3 = " + findMember3);

		em.flush();
		em.clear();

		// 엔티티의 ID를 사용할 때 (외래키)
		String useEntityFKQuery = "SELECT m FROM Member m WHERE m.team.id = :teamId";
		Member findMember4 = em.createQuery(useEntityFKQuery, Member.class)
				.setParameter("teamId", teamA.getId())
				.getSingleResult();
		System.out.println("findMember4 = " + findMember4);
	}

	private static void fetchJoinExample(EntityManager em) {

		Team teamA = new Team();
		teamA.setName("teamA");
		em.persist(teamA);

		Member member1 = new Member();
		member1.setUserName("회원1");
		member1.setTeam(teamA);
		em.persist(member1);

		Member member2 = new Member();
		member2.setUserName("회원2");
		member2.setTeam(teamA);
		em.persist(member2);

		Team teamB = new Team();
		teamB.setName("teamB");
		em.persist(teamB);

		Member member3 = new Member();
		member3.setUserName("회원3");
		member3.setTeam(teamB);
		em.persist(member3);

		em.flush();
		em.clear();

		// ENTITY JOIN
		String query = "SELECT m FROM Member m";
		List<Member> resultList1 = em.createQuery(query, Member.class).getResultList();

		for (Member member : resultList1) {
			System.out.println("Entity Loop1 member = " + member.getUserName() + ", team = " + member.getTeam().getName());
			// 멤버1 : 첫 루프를 돌면서 쿼리를 사용하여 TeamA를 가져옴 (영속성 컨텍스트에 없기 때문에)
			// 멤버2 : TeamA를 영속성 컨텍스트(1차캐시)에서 가져옴
			// 멤버3 : 쿼리를 사용하여 TeamB를 가져옴 (마찬가지로 영속성 컨텍스트에 없기 때문에)

			// 만약 회원이 100명이라면 쿼리를 100번 실행함 (최악의 경우에 팀이 다 다를 경우)
			// N + 1 : (Team) + (Member)
		}

		em.flush();
		em.clear();

		// ENTITY JOIN FETCH
		String entityFetchJoinQuery = "SELECT m FROM Member m JOIN FETCH m.team";
//		String entityFetchJoinQuery = "SELECT m FROM Member m LEFT JOIN FETCH m.team";
		List<Member> resultList2 = em.createQuery(entityFetchJoinQuery, Member.class).getResultList();

		for (Member member : resultList2) {
			// 조인해서 Team 데이터도 함께 가져오기 때문에 영속성 컨텍스트에 다 있음 (team은 프록시가 아님)
			System.out.println("Entity Loop2 member = " + member.getUserName() + ", team = " + member.getTeam().getName());
		}

		em.flush();
		em.clear();

		// COLLECTION JOIN FETCH
		String collectionFetchJoinQuery = "SELECT t FROM Team t JOIN FETCH t.members";
		List<Team> resultList3 = em.createQuery(collectionFetchJoinQuery, Team.class).getResultList();

		for (Team team : resultList3) {
			// 콘솔 출력 결과
			// teamA 중복 출력 (조인으로 인해서)
//			Collection Loop1 team = teamA, members size = 2  // TEAM A - MEMBER 1
//			Collection Loop1 team = teamA, members size = 2  // TEAM A - MEMBER 2
//			Collection Loop1 team = teamB, members size = 1  // TEAM B - MEMBER 3
			System.out.println("Collection Loop1 team = " + team.getName() + ", members size = " + team.getMembers().size());

			// 멤버 확인
			for (Member member : team.getMembers()) {
				System.out.println("Members -> " + member);
			}
		}

		em.flush();
		em.clear();

		// DISTINCT
		// size 2
		String noDistinctQuery1 = "SELECT t FROM Team t";
		List<Team> noDistinctList1 = em.createQuery(noDistinctQuery1, Team.class).getResultList();
		System.out.println("noDistinctList1 size : " + noDistinctList1.size()); // noDistinctList1.size() = 3

		// size 3
		String noDistinctQuery2 = "SELECT t FROM Team t JOIN FETCH t.members";
		List<Team> noDistinctList2 = em.createQuery(noDistinctQuery2, Team.class).getResultList();
		System.out.println("noDistinctList2 size : " + noDistinctList2.size()); // noDistinctList2.size() = 3

		// distinct query
		// 현재 예제는 SQL 쿼리만으론 Distinct가 의미가 없음
		// Distinct를 추가하면 JPA가 앱에서 중복 제거 시도
		// 같은 식별자를 가진 Team 엔티티 제거
		String distinctFetchJoinQuery = "SELECT DISTINCT t FROM Team t JOIN FETCH t.members"; // resultList4.size() = 3
		List<Team> distinctList = em.createQuery(distinctFetchJoinQuery, Team.class).getResultList();

		System.out.println("distinctList size : " + distinctList.size());

		for (Team team : distinctList) {
			System.out.println("distinct team = " + team.getName() + ", members size = " + team.getMembers().size());
		}

		em.flush();
		em.clear();

		// 일반 조인
		// members가 지연로딩
		// 여기서는 팀 엔티티만 조회, 회원 엔티티는 조회하지 않음
		String basicJoinQuery = "SELECT t FROM Team t JOIN t.members m";
		List<Team> basicJoinList = em.createQuery(basicJoinQuery, Team.class).getResultList();

		em.flush();
		em.clear();

		// 페이징
		String unrighteousQuery = "SELECT t FROM Team t JOIN FETCH t.members m"; // 올바르지 않은 쿼리

		// 뒤집어서 Member로 시작하여 페이징
		String rightQuery = "SELECT m FROM Member m JOIN FETCH m.team t";

		List<Member> pagingList = em.createQuery(rightQuery, Member.class)
				.setFirstResult(0)
				.setMaxResults(1)
				.getResultList();

		for (Member member : pagingList) {
			System.out.println("pagingList member = " + member.getUserName() + ", team = " + member.getTeam().getName());
		}

		em.flush();
		em.clear();

		// 페이징을 억지로 구현할 때
		String unrighteousQuery2 = "SELECT t FROM Team t ";

		List<Team> pagingList2 = em.createQuery(unrighteousQuery2, Team.class)
				.setFirstResult(0)
				.setMaxResults(2)
				.getResultList();

		System.out.println("pagingList2 size : " + pagingList2.size());

		// Member 데이터 lazy loading으로 인하여 성능이 좋지 않음
		for (Team team : pagingList2) {
			System.out.println("pagingList2 team = " + team.getName() + ", members size = " + team.getMembers().size());
			for (Member member : team.getMembers()) {
				System.out.println("-> member = " + member);
			}
		}

		// 따라서 Team 클래스에 List<Member> members 필드에 @BatchSize(size = 100) 어노테이션 태깅
	}

	private static void pathExpressionExample(EntityManager em) {

		Team team1 = new Team();
		team1.setName("team1");
		em.persist(team1);

		Team team2 = new Team();
		team2.setName("team2");
		em.persist(team2);

		Member member1 = new Member();
		member1.setUserName("member1");
		member1.setAge(10);
		member1.setTeam(team1);
		em.persist(member1);

		Member member2 = new Member();
		member2.setUserName("member2");
		member2.setAge(20);
		member2.setTeam(team2);

		em.persist(member2);

		em.flush();
		em.clear();

		// 상태 필드
		String stateQuery = "SELECT m.userName FROM Member m";
		List<String> resultList1 = em.createQuery(stateQuery, String.class).getResultList();
		for (String s : resultList1) {
			System.out.println("result1 String : " + s);
		}

		// 단일 값 연관 경로 - 탐색 가능
		// 묵시적 내부 조인 발생
		String oneValueQuery = "SELECT m.team FROM Member m";
		List<Team> resultList2 = em.createQuery(oneValueQuery, Team.class).getResultList();
		for (Team t : resultList2) {
			System.out.println("resultList2 Team : " + t);
		}

		// 컬렉션 값 연관 경로 - 탐색 불가능 (컬렉션 자체를 가리키기 때문에 그 안에 필드 정보 등을 가져올 수 없음)
		// 묵시적 내부 조인 발생
		String sizeQuery = "SELECT t.members.size FROM Team t";
		Integer resultList4 = em.createQuery(sizeQuery, Integer.class).getSingleResult();
		System.out.println("Team members size : " + resultList4);

		String collectionValueQuery = "SELECT t.members FROM Team t";
		Collection resultList3 = em.createQuery(collectionValueQuery, Collection.class).getResultList();
		for (Object o : resultList3) {
			System.out.println("resultList3 Object : " + o);
		}

		String joinQuery = "SELECT m FROM Team t join t.members m";
		List<Member> resultList5 = em.createQuery(joinQuery, Member.class).getResultList();
		for (Member m : resultList5) {
			System.out.println("resultList5 Member : " + m);
		}
	}

	private static void functionExample(EntityManager em) {

		Member member1 = new Member();
		member1.setUserName("member1");
		member1.setAge(10);
		member1.setType(MemberType.ADMIN);

		em.persist(member1);

		Member member2 = new Member();
		member2.setUserName("member2");
		member2.setAge(20);
		member2.setType(MemberType.USER);

		em.persist(member2);

		em.flush();
		em.clear();

		// ||
		String query = "SELECT 'a' || 'b' FROM Member m";
		List<String> resultList1 = em.createQuery(query).getResultList();
		for (String s : resultList1) {
			System.out.println("result1 String : " + s);
		}

		// concat
		String concatQuery = "SELECT CONCAT('a', 'b') FROM Member m";
		List<String> resultList2 = em.createQuery(concatQuery).getResultList();
		for (String s : resultList2) {
			System.out.println("resultList2 String : " + s);
		}

		// substring
		String subStringQuery = "SELECT SUBSTRING(m.userName, 2, 3) FROM Member m";
		List<String> resultList3 = em.createQuery(subStringQuery).getResultList();
		for (String s : resultList3) {
			System.out.println("resultList3 String : " + s);
		}

		// locate
		String locateQuery = "SELECT LOCATE('de', 'abcdefg') FROM Member m";
		List<Integer> resultList4 = em.createQuery(locateQuery).getResultList();
		for (Integer intValue : resultList4) {
			System.out.println("resultList4 intValue : " + intValue);
		}

		// SIZE - 컬렉션의 크기를 돌려주는 함수
		String sizeQuery = "SELECT SIZE(t.members) FROM Team t";
		List<Integer> resultList5 = em.createQuery(sizeQuery).getResultList();
		for (Integer intValue : resultList5) {
			System.out.println("resultList5 intValue : " + intValue);
		}

		// INDEX - 컬렉션의 위치를 구할 때 사용 (사용을 권장하진 않음)
		// @OrderColumn 어노테이션을 사용할 때 사용
		// 사용하려면 Team 객체에 members 필드에 @OrderColumn 어노테이션 태깅
//		String indexQuery = "SELECT INDEX(t.members) FROM Team t";
//		List<Integer> resultList6 = em.createQuery(indexQuery).getResultList();
//		for (Integer intValue : resultList6) {
//			System.out.println("resultList6 intValue : " + intValue);
//		}

		// 사용자 정의 함수
//		String userFunctionQuery = "SELECT FUNCTION('group_concat', m.userName) FROM Member m";
		String userFunctionQuery = "SELECT group_concat(m.userName) FROM Member m";
		List<String> resultList7 = em.createQuery(userFunctionQuery).getResultList();
		for (String s : resultList7) {
			System.out.println("resultList7 String : " + s);
		}
	}

	private static void caseExpressionExample(EntityManager em) {

		Member member = new Member();
		member.setUserName("관리자");
		member.setAge(10);
		member.setType(MemberType.ADMIN);

		em.persist(member);

		em.flush();
		em.clear();

		// CASE
		String caseQuery1 =
				"SELECT " +
						" CASE WHEN m.age <= 10 THEN '학생요금' " +
						"      WHEN m.age >= 60 THEN '경로요금' " +
						"      ELSE '일반요금' " +
						"  END " +
						" FROM Member m";
		List<String> resultList1 = em.createQuery(caseQuery1).getResultList();
		for (String s : resultList1) {
			System.out.println("resultList1 String = " + s);
		}

		// COALESCE
		String coalesceQuery = "SELECT COALESCE(m.userName, '이름 없는 회원') as userName FROM Member m";
		List<String> resultList2 = em.createQuery(coalesceQuery).getResultList();
		for (String s : resultList2) {
			System.out.println("resultList2 String = " + s);
		}

		// NULLIF
		String nullIfQuery = "SELECT NULLIF(m.userName, '관리자') as userName FROM Member m"; // 이름이 관리자면 NULL 반환
		List<String> resultList3 = em.createQuery(nullIfQuery).getResultList();
		for (String s : resultList3) {
			System.out.println("resultList3 String = " + s);
		}
	}

	private static void typeExpressionExample(EntityManager em) {

		Member member = new Member();
		member.setUserName("member");
		member.setAge(10);
		member.setType(MemberType.ADMIN);

		em.persist(member);

		em.flush();
		em.clear();

		String exampleQuery1 =
				"SELECT m.userName, 'HELLO', TRUE FROM Member m WHERE m.type = com.jpql.entity.MemberType.ADMIN";
		List<Object[]> resultList1 = em.createQuery(exampleQuery1).getResultList();

		// 파라미터 바인딩으로 변경
		String exampleQuery2 =
				"SELECT m.userName, 'HELLO', TRUE FROM Member m WHERE m.type = :userType";
		List<Object[]> resultList2 = em.createQuery(exampleQuery2)
				.setParameter("userType", MemberType.ADMIN)
				.getResultList();

		for (Object[] objects : resultList2) {
			System.out.println("Objects 0 = " + objects[0]);
			System.out.println("Objects 1 = " + objects[1]);
			System.out.println("Objects 2 = " + objects[2]);
		}

		// Entity Type
		// 다형성
		String entityQuery = "SELECT i FROM Item i WHERE type(i) = Book";
		em.createQuery(entityQuery, Item.class).getResultList();
	}

	private static void joinExample(EntityManager em) {

		Team team = new Team();
		team.setName("Team A");
		em.persist(team);

		Member member = new Member();
		member.setUserName("member");
		member.setAge(10);

		member.setTeam(team);

		em.persist(member);

		em.flush();
		em.clear();

		// JOIN

		String queryStr = "select m from Member m inner join m.team t ";
//		String queryStr = "select m from Member m inner join m.team t where t.name = : teamName"; // where
//		String queryStr = "select m from Member m left outer join m.team t "; // outer join
//		String queryStr = "select m from Member m, Team t where m.userName = t.name"; // seta(cross) join

		List<Member> memberResult1 = em.createQuery(queryStr, Member.class).getResultList();

		System.out.println("memberResult1 size : " + memberResult1.size());

		for (Member resultMember : memberResult1) {
			System.out.println("memberResult1 member : " + resultMember);
		}

		// ON
//		String queryOnStr = "select m from Member m inner join m.team t on t.name = 'teamA'";
		String queryOnStr = "select m from Member m inner join Team t on m.userName = t.name";
		List<Member> memberResult2 = em.createQuery(queryOnStr, Member.class).getResultList();

		System.out.println("memberResult2 size : " + memberResult2.size());

		for (Member resultMember : memberResult2) {
			System.out.println("memberResult2 member : " + resultMember);
		}
	}

	private static void pagingExample(EntityManager em) {

		// Member 테이블의 ID 값(PK)는 1번부터 들어감
		for (int i = 0; i < 100; i++) {
			Member member = new Member();
			member.setUserName("member " + i);
			member.setAge(i);

			em.persist(member);
		}

		em.flush();
		em.clear();

		List<Member> memberResult = em.createQuery("select m from Member m order by m.age desc", Member.class)
//				.setFirstResult(0) // limit ?
				.setFirstResult(1) // limit ? offset ?
				.setMaxResults(10)
				.getResultList();

		System.out.println("memberResult size : " + memberResult.size());

		for (Member resultMember : memberResult) {
			System.out.println("member : " + resultMember);
		}
	}

	private static void projectionExample(EntityManager em) {

		Member member = new Member();
		member.setUserName("member");
		member.setAge(10);

		em.persist(member);

		em.flush();
		em.clear();

		List<Member> resultList =
				em.createQuery("select m from Member m ", Member.class).getResultList();

		Member findMember = resultList.get(0);
		findMember.setAge(20);

		// 조인 쿼리 실행
		// 실행되는 SQL과 작성한 JPQL을 최대한 동일하게 작성하는 것을 추천 (명시적 조인)

		// 묵시적 조인
//		List<Team> teamResultList =
//				em.createQuery("select m.team from Member m ", Team.class).getResultList();
		// 명시적 조인
		List<Team> teamResultList =
				em.createQuery("select t from Member m join m.team t", Team.class).getResultList();

//		Team findTeam = teamResultList.get(0);

		// 임베디드 타입
		List<Address> orderResultList =
				em.createQuery("select o.address from Order o", Address.class).getResultList();

		// 스칼라 타입
		// Object[]로 조회
		List<Object> scalaResultList1 =
				em.createQuery("select distinct m.userName, m.age from Member m ").getResultList();

		Object o = scalaResultList1.get(0);
		Object[] scalaObjList1 = (Object[]) o;
		System.out.println("scala 1 userName " + scalaObjList1[0]);
		System.out.println("scala 1 age " + scalaObjList1[1]);

		List<Object[]> scalaResultList2 =
				em.createQuery("select distinct m.userName, m.age from Member m ").getResultList();
		Object[] scalaObjList2 = scalaResultList2.get(0);
		System.out.println("scala 2 userName " + scalaObjList2[0]);
		System.out.println("scala 2 age " + scalaObjList2[1]);

		// new 키워드 사용하여 조회시 조회 필드에 맞는 생성자 필요
		// 패키지명까지 다 기술해야함
		List<MemberDTO> memberDTOResultList =
				em.createQuery("select new com.jpql.DTO.MemberDTO(m.userName, m.age) from Member m ", MemberDTO.class)
						.getResultList();

		MemberDTO resultMemberDTO = memberDTOResultList.get(0);
		System.out.println("resultMemberDTO userName " + resultMemberDTO.getUserName());
		System.out.println("resultMemberDTO age " + resultMemberDTO.getAge());
	}

	private static void bindParameterExample(EntityManager em) {

		Member member = new Member();
		member.setUserName("member");
		member.setAge(10);

		em.persist(member);

		// 파라미터 바인딩 (가급적 이름(문자) 기준으로 사용)
//			TypedQuery<Member> memberTypedQuery1 =
//					em.createQuery("select m from Member m where m.userName = : userName", Member.class);
//			memberTypedQuery1.setParameter("userName", "member");
//			memberTypedQuery1.getSingleResult();

		// 메소드 체이닝 사용
		Member memberTypedQuery1 =
				em.createQuery("select m from Member m where m.userName = : userName", Member.class)
						.setParameter("userName", "member")
						.getSingleResult();

		System.out.println("result = " + memberTypedQuery1);
	}

	private static void getResultExample(EntityManager em) {

		Member member = new Member();
		member.setUserName("member");
		member.setAge(10);

		em.persist(member);

		// 값이 여러 개일 때
		TypedQuery<Member> memberTypedQuery =
				em.createQuery("select m from Member m ", Member.class);
		List<Member> resultList = memberTypedQuery.getResultList();
		for (Member member1 : resultList) {
			System.out.println("member : " + member1);
		}

		// 값이 한 개일 때 (정확히 딱 한 개만 있을때 사용할 것)
		// 값이 여러개 또는 없을 경우 Exception 발생
		// Spring Data JPA > Null or Optional 반환 (Exception 반환하지 않음)
		Member resultMember = memberTypedQuery.getSingleResult();
		System.out.println("member : " + resultMember);
	}

	private static void queryTypeExample(EntityManager em) {

		Member member = new Member();
		member.setUserName("member");
		member.setAge(10);

		em.persist(member);

		// 타입 값이 명확함
		TypedQuery<Member> typedQuery1 =
				em.createQuery("select m from Member m where m.userName = : userName", Member.class);
		TypedQuery<String> typedQuery2 =
				em.createQuery("select m.userName from Member m ", String.class);

		// 타입 값이 명확하지 않음
		Query query = em.createQuery("select m.userName, m.age from Member m ");
	}
}
