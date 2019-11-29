package com.mie.main.intro.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity
@Table(name = "PROFESSOR_HISTORY")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createAt", "updateAt"}, allowGetters = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Historys {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "professor_history_seq")
	@SequenceGenerator(sequenceName = "SEQUENCE_PROFESSOR_HISTORY", allocationSize = 1, name="professor_history_seq")
	private int id;
	
	private String history; // 날짜
	
	private String name; // 히스토리 이름
	
	@Column(name = "professor_id")
	private int professorId;
	
	public Historys(String history, String name) {
		this.name = name;
		this.history = history;
	}
}
