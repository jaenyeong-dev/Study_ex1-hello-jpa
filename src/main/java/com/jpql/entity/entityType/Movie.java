package com.jpql.entity.entityType;

import javax.persistence.Entity;

@Entity
public class Movie extends Item {

	private String director;
	private String actor;

	public String getDirector() {
		return director;
	}

	public Movie setDirector(String director) {
		this.director = director;
		return this;
	}

	public String getActor() {
		return actor;
	}

	public Movie setActor(String actor) {
		this.actor = actor;
		return this;
	}
}
