package com.mie.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mie.main.member.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	
	Member findByusername(String username);
}
