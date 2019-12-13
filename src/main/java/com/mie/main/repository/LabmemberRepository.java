package com.mie.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mie.main.board.model.Board;
import com.mie.main.labmember.model.Labmember;

public interface LabmemberRepository extends JpaRepository<Labmember, Long> {
	Labmember findByid(Long id);
	List<Labmember> findBytype(String type);
}
