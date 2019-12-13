package com.mie.main.labmember.model;

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
import com.mie.main.board.model.Board;
import com.mie.main.intro.model.History;
import com.mie.main.intro.model.Professor;
import com.mie.main.intro.model.History.HistoryBuilder;
import com.mie.main.member.model.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity
@Table(name = "ACTIVITIES")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createAt", "updateAt", "labmember"}, allowGetters = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Activities {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "activities_seq")
	@SequenceGenerator(sequenceName = "SEQUENCE_ACTIVITIES", allocationSize = 1, name="activities_seq")
	private Long activitie_id;
	
	private String title;
	
	private int type;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private Labmember labmember;
	
	//JOIN 테이블이 Json 결과에 표시되지 않도록 처리.
	protected Labmember getLabmember() {
		return labmember;
	}
	
	public Activities(Long activitie_id, String title, int type) {
		this.activitie_id = activitie_id;
		this.title = title;
		this.type = type;
	}
	
	public Activities(String title, int type) {
		this.title = title;
		this.type = type;
	}
//	//생성자
//	public Activities(Labmember labmember, String title, int type) {
//		this.labmember = labmember;
//		this.title = title;
//		this.type = type;
//	}
}
