package com.prograngers.backend.repository.solution;

import com.prograngers.backend.entity.member.Member;
import com.prograngers.backend.entity.solution.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long>, QueryDslSolutionRepository{
    List<Solution> findAllByMember(Member member);
    List<Solution> findAllByScrapSolution(Solution solution);
    List<Solution> findTop3ByMemberOrderByCreatedDateDesc(Member member);


}
