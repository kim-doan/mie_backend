package com.mie.main.labmember.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mie.main.board.model.Board;
import com.mie.main.board.model.Board.BoardBuilder;
import com.mie.main.intro.model.Activity;
import com.mie.main.intro.model.History;
import com.mie.main.intro.model.Professor;
import com.mie.main.intro.model.Professor.ProfessorBuilder;
import com.mie.main.member.model.Member;
import com.mie.main.member.model.Member.MemberBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity
@Table(name = "LABMEMBER")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createAt", "updateAt", "labmember"}, allowGetters = true)
@EqualsAndHashCode(exclude = "activities")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Labmember {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "labmembers_seq")
	@SequenceGenerator(sequenceName = "SEQUENCE_LABMEMBER", allocationSize = 1, name="labmembers_seq")
	private Long id;
	
	private String name;
	private String enname;
	private String admission;
	private String email;
	private String field;
	private String workplace;
	private String type;
	private String profile_img;
	private String oneword;
	
	@OneToMany(mappedBy = "labmember", cascade = CascadeType.ALL)
	private Set<Activities> activities;
	
	
	public Labmember(Long id, String name, String enname, String admission, String email, String field, String workplace, String type,
			String profile_img, String oneword, Activities... activities) {
		this.id = id;
		this.name = name;
		this.enname = enname;
		this.admission = admission;
		this.email = email;
		this.field = field;
		this.workplace = workplace;
		this.type = type;
		this.profile_img = profile_img;
		this.oneword = oneword;
		this.activities = Stream.of(activities).collect(Collectors.toSet());
		this.activities.forEach(x -> x.setLabmember(this));
	}
	
	public Labmember(String name, String enname, String admission, String email, String field, String workplace, String type, String profile_img, String oneword, Activities... activities) {
		this.name = name;
		this.enname = enname;
		this.admission = admission;
		this.email = email;
		this.field = field;
		this.workplace = workplace;
		this.type = type;
		this.profile_img = profile_img;
		this.oneword = oneword;
		this.activities = Stream.of(activities).collect(Collectors.toSet());
		this.activities.forEach(x -> x.setLabmember(this));
	}
	
//	public Labmember(String name, String enname, String admission, String email, String field, String workplace, String type, String profile_img) {
//		this.name = name;
//		this.enname = enname;
//		this.admission = admission;
//		this.email = email;
//		this.field = field;
//		this.workplace = workplace;
//		this.type = type;
//		this.profile_img = profile_img;
//	}

	@Override
	public String toString() {
		return "Labmember [id=" + id + ", name=" + name + ", enname=" + enname + ", admission=" + admission + ", email="
				+ email + ", field=" + field + ", workplace=" + workplace + ", type=" + type + ", profile_img="
				+ profile_img + "]";
	}
}
