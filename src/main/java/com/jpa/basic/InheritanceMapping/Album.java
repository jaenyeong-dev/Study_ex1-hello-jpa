package com.jpa.basic.InheritanceMapping;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "A") // default entity(class) name
public class Album extends Item {

	private String artist;

	public String getArtist() {
		return artist;
	}

	public Album setArtist(String artist) {
		this.artist = artist;
		return this;
	}
}
