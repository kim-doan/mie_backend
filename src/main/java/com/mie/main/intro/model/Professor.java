package com.mie.main.intro.model;

import java.util.ArrayList;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mie.main.intro.model.Intro.IntroBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity
@Table(name = "PROFESSOR")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createAt", "updateAt"}, allowGetters = true)
@EqualsAndHashCode(exclude = "historys")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Professor {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "professor_seq")
	@SequenceGenerator(sequenceName = "SEQUENCE_PROFESSOR", allocationSize = 1, name="professor_seq")
	private int id;
	private String name;
	private String position;
	private String tel;
	private String email;
	private String hobby;
	private String family;
	
	@OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
	private Set<History> historys;
	
	@OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
	private Set<Activity> activitys;
	
	public Professor(int id, String name, String position, String tel, String email, String hobby, String family, History... historys) {
		this.id = id;
		this.name = name;
		this.position = position;
		this.tel = tel;
		this.email = email;
		this.hobby = hobby;
		this.family = family;
		this.historys = Stream.of(historys).collect(Collectors.toSet());
		this.historys.forEach(x -> x.setProfessor(this));
	}
	
	public Professor(int id, String name, String position, String tel, String email, String hobby, String family, Activity... activitys) {
		this.id = id;
		this.name = name;
		this.position = position;
		this.tel = tel;
		this.email = email;
		this.hobby = hobby;
		this.family = family;
		this.activitys = Stream.of(activitys).collect(Collectors.toSet());
		this.activitys.forEach(x -> x.setProfessor(this));
	}
}
