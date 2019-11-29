package com.mie.main.date;

import java.time.LocalDateTime;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
//날짜 필드 상속
public abstract class CommonDateEntity {
	@CreatedDate // Entity 생성시 자동으로 날짜 세팅
	private LocalDateTime createAt;
	@LastModifiedDate // Entity 수정시 자동으로 날짜 세팅
	private LocalDateTime modifiedAt;
}
