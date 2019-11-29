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
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CustomProfessor {
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
	private String profile_img;
}
