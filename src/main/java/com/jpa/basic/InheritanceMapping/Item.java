package com.jpa.basic.InheritanceMapping;

import javax.persistence.*;

@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // SINGLE_TABLE 속성은 @DiscriminatorColumn 없더라도 자동으로 DTYPE 컬럼이 생성됨
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // TABLE_PER_CLASS 속성은 @DiscriminatorColumn이 필요 없음
@DiscriminatorColumn // DTYPE - 하위 데이터(테이블) 타입
public abstract class Item {

	@Id
	@GeneratedValue
	private Long id;

	private String name;
	private int price;

	public Long getId() {
		return id;
	}

	public Item setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Item setName(String name) {
		this.name = name;
		return this;
	}

	public int getPrice() {
		return price;
	}

	public Item setPrice(int price) {
		this.price = price;
		return this;
	}
}
