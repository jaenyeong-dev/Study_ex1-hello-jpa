package com.jpabook.jpashop;

import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderItem;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaShopMain {

	public static void main(String[] args) {
		// persistenceUnitName은 persistence.xml에 persistence-unit 태그의 이름 속성값을 줌
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpaShop");

		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();

		try {

			Order order = new Order();
//			order.addOrderItem(new OrderItem());

			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);

			em.persist(orderItem);

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
