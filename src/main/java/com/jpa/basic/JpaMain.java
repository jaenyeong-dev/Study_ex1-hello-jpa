package com.jpa.basic;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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

			// insert
//			Member member = new Member();
//			member.setId(1L);
//			member.setName("helloA");
//			member.setId(2L);
//			member.setName("helloB");
//			em.persist(member);

			// update
			Member findMember = em.find(Member.class, 1L);
			System.out.println("findMember id: " + findMember.getId());
			System.out.println("findMember name: " + findMember.getName());
			findMember.setName("Hello JPA");

			// delete
//			em.remove(findMember);

			// JPQL
			List<Member> result = em.createQuery("select m from Member as m", Member.class)
					.setFirstResult(5)
					.setMaxResults(8)
					.getResultList();

			for (Member mem : result) {
				System.out.println("member.name : " + mem.getName());
			}

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
