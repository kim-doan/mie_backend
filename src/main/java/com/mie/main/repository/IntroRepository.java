package com.mie.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mie.main.intro.model.Intro;

public interface IntroRepository extends JpaRepository<Intro, String>{

}
