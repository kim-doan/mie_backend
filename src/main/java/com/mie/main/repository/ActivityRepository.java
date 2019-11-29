package com.mie.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mie.main.intro.model.Activitys;
import com.mie.main.intro.model.Historys;

public interface ActivityRepository extends JpaRepository<Activitys, Integer>{
	List<Activitys> findByprofessorId(int professorId);
}
