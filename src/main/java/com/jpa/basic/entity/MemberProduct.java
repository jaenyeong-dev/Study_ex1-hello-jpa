package com.jpa.basic.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

// 연결 테이블용 엔티티
@Entity
public class MemberProduct {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "MEMBER_ID")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;

	private int count;
	private int price;

	private LocalDateTime orderDateTime;
}
