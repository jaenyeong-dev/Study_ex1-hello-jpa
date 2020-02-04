package com.jpa.basic;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

	public static void main(String[] args) {
		// Name은 persistence.xml에 persistence-unit 태그의 이름 속성값을 줌
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpaBasic");

		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		// execute code
		try {

			// insert (비영속)
//			Member member = new Member();
//			member.setId(1L);
//			member.setName("helloA");
//			member.setId(2L);
//			member.setName("helloB");

//			Member member = new Member(101L, "TEST");
//			em.persist(member);


			// 영속
//			System.out.println(">> Before <<");
//			em.persist(member);
//			System.out.println(">> After <<");

			// 준영속
//			em.detach(member);

			// update
//            Member findMember1 = em.find(Member.class, 1L);
//            Member findMember2 = em.find(Member.class, 1L);
//            System.out.println("findMember id: " + findMember1.getId());
//            System.out.println("findMember name: " + findMember1.getUserName());
//			  findMember1.setName("Hello JPA");

			// 영속 엔티티 동일성 보장
//            System.out.println("result = " + (findMember1 == findMember2));

			// delete
//			em.remove(findMember);

			// JPQL
//			List<Member> result = em.createQuery("select m from Member as m", Member.class)
//					.setFirstResult(5)
//					.setMaxResults(8)
//					.getResultList();
//
//			for (Member mem : result) {
//				System.out.println("member.name : " + mem.getName());
//			}

            Member memberA = new Member();
//            member.setId("ID_A");
			memberA.setUserName("A");

			Member memberB = new Member();
			memberB.setUserName("B");

			Member memberC = new Member();
			memberC.setUserName("C");

			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            em.persist(memberA);  // 1, 51
			em.persist(memberB);  // memory
			em.persist(memberC);  // memory

			System.out.println("member A " + memberA.getId());
			System.out.println("member B " + memberB.getId());
			System.out.println("member C " + memberC.getId());

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
}
