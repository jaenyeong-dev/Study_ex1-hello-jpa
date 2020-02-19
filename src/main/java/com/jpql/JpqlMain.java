package com.jpql;

import com.jpql.DTO.MemberDTO;
import com.jpql.entity.Address;
import com.jpql.entity.Member;
import com.jpql.entity.MemberType;
import com.jpql.entity.Team;
import com.jpql.entity.entityType.Item;

import javax.persistence.*;
import java.util.List;

public class JpqlMain {

	public static void main(String[] args) {
		// Name은 persistence.xml에 persistence-unit 태그의 이름 속성값을 줌
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlBasic");

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

			functionExample(em);

			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {

			em.close();
		}
		emf.close();
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
