package com.jpql.DTO;

public class MemberDTO {

	private String userName;
	private int age;

	// JPQL에서 New 키워드로 조회시 해당 조회 필드에 맞는 생성자 필요
	public MemberDTO(String userName, int age) {
		this.userName = userName;
		this.age = age;
	}

	public String getUserName() {
		return userName;
	}

	public MemberDTO setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	public int getAge() {
		return age;
	}

	public MemberDTO setAge(int age) {
		this.age = age;
		return this;
	}
}
