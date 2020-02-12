package com.jpa.basic.entity;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class Period {
	private LocalDateTime startDate;
	private LocalDateTime endDate;

//	public boolean isWork() {
//	}

	public Period() {
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public Period setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
		return this;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public Period setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
		return this;
	}
}
