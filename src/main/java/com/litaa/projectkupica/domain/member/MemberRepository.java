package com.litaa.projectkupica.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author : Unagi_zoso
 * @date : 2022-10-03
 */
public interface MemberRepository extends JpaRepository<Member, Integer> {
}
