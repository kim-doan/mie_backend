package com.mie.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mie.main.intro.model.CustomProfessor;
import com.mie.main.intro.model.Professor;

public interface CustomProfessorRepository extends JpaRepository<CustomProfessor, Integer> {
	CustomProfessor findByid(int id);
}
