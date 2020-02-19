package com.jpql.entity.entityType;

import javax.persistence.Entity;

@Entity
public class Book extends Item {

	private String author;
	private String isbn;

	public String getAuthor() {
		return author;
	}

	public Book setAuthor(String author) {
		this.author = author;
		return this;
	}

	public String getIsbn() {
		return isbn;
	}

	public Book setIsbn(String isbn) {
		this.isbn = isbn;
		return this;
	}
}
