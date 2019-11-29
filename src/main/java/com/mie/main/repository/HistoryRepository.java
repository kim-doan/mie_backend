package com.mie.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Optionals;

import com.mie.main.intro.model.History;
import com.mie.main.intro.model.Historys;

public interface HistoryRepository extends JpaRepository<Historys, Integer>{
	List<Historys> findByprofessorId(int professorId);
}
