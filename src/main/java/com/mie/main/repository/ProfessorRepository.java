package com.mie.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mie.main.intro.model.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Integer>{
	Professor findByid(int id);
}
