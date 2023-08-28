package com.prograngers.backend.dto.review.response;

import com.prograngers.backend.entity.solution.Solution;
import com.prograngers.backend.entity.solution.AlgorithmConstant;
import com.prograngers.backend.entity.solution.DataStructureConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class SolutionReviewsResponse {
    private String title;
    private AlgorithmConstant algorithm;
    private DataStructureConstant dataStructure;
    private String solution;
    private List<Line> lines = new ArrayList<>();

    public static SolutionReviewsResponse from(Solution solution, String[] lines) {
        SolutionReviewsResponse solutionReviewsResponse = new SolutionReviewsResponse();
        solutionReviewsResponse.title = solution.getTitle();
        solutionReviewsResponse.algorithm = solution.getAlgorithm();
        solutionReviewsResponse.dataStructure = solution.getDataStructure();
        // 먼저 최종 응답 dto에 각 라인을 넣는다
        for (int i = 0; i < lines.length; i++) {
            Line line = Line.builder()
                    .codeLineNumber(i + 1)
                    .code(lines[i])
                    .build();
            solutionReviewsResponse.getLines().add(line);
        }
        return  solutionReviewsResponse;
    }

}
