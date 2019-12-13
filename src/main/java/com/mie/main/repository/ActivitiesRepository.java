package com.mie.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mie.main.board.model.Board;
import com.mie.main.board.model.Post;
import com.mie.main.labmember.model.Activities;
import com.mie.main.labmember.model.Labmember;

public interface ActivitiesRepository extends JpaRepository<Activities, Long> {
	List<Activities> findByLabmember(Labmember labmember);
}
