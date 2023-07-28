package com.prograngers.backend.dto;

import com.prograngers.backend.entity.*;
import com.prograngers.backend.exception.notfound.ProblemLinkNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolutionRequest {

    @NotBlank(message = "문제 제목을 입력해주세요")
    private String problemTitle;
    @NotBlank(message = "풀이 제목을 입력해주세요")
    private String solutionTitle;
    @NotBlank(message = "문제 링크를 입력해주세요")
    private String problemLink;
    // @NotBlank(message = "문제 난이도를 입력해주세요")
    private Levels level;

    // @NotBlank(message = "알고리즘을 입력해주세요")
    private Algorithms algorithm;

    // @NotBlank(message = "자료구조를 입력해주세요")
    private DataStructures dataStructure;

    @NotBlank(message = "풀이 설명을 입력해주세요")
    private String description;

    @NotBlank(message = "소스 코드를 입력해주세요")
    private String code;

    public Solution toEntity(){
        /*
        파싱해서 ojname 알아내서 문제에 넣기
        로그인정보로 멤버 알아내서 넣기
        스크랩 여부 알아내서 넣기
         */

        // 입력 링크 파싱해서 저지 정보 알아내기 아닐 경우 ProblemLinkNotFoundException
        String ojName = checkLink(problemLink);

        return Solution.builder()
                .problem(new Problem(null, problemTitle, problemLink, ojName)) // 파싱해서 ojname 알아내야함
                .member(new Member()) // 로그인정보로 멤버를 알아내야함
                .title(solutionTitle)
                .isPublic(true)
                .code(code)
                .description(description)
                .scraps(0)
                .date(LocalDate.now())
                .level(level)
                .algorithm(new Algorithm(null,algorithm))
                .dataStructure(new DataStructure(null,dataStructure))
                .description(description)
                .code(code)
                .build();
    }

    private String checkLink(String problemLink) {
        if (problemLink.contains("acmicpc.net/problem")){
            return "백준";
        } else if (problemLink.contains("programmers.co.kr/learn/courses")){
            return "프로그래머스";
        } else{
            throw new ProblemLinkNotFoundException();
        }
    }
}
