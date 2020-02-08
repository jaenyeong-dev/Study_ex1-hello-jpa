package com.jpabook.jpashop.domain;

import javax.persistence.Entity;

@Entity
public class Album extends Item {

	private String artist;
	private String etc;

	public String getArtist() {
		return artist;
	}

	public Album setArtist(String artist) {
		this.artist = artist;
		return this;
	}

	public String getEtc() {
		return etc;
	}

	public Album setEtc(String etc) {
		this.etc = etc;
		return this;
	}
}
