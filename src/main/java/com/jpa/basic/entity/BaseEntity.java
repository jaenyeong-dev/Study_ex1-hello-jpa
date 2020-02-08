package com.jpa.basic.entity;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

//	@Column(name = "INSERT_MEMBER") // 컬럼명 변경 가능
	private String createdBy;
	private LocalDateTime createdDate;
//	@Column(name = "UPDATE_MEMBER") // 컬럼명 변경 가능
	private String lastModifiedBy;
	private LocalDateTime lastModifiedDate;

	public String getCreatedBy() {
		return createdBy;
	}

	public BaseEntity setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
		return this;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public BaseEntity setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
		return this;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public BaseEntity setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
		return this;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public BaseEntity setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
		return this;
	}
}
