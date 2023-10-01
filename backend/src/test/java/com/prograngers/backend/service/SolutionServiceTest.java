package com.prograngers.backend.service;

import com.prograngers.backend.dto.solution.reqeust.ScarpSolutionRequest;
import com.prograngers.backend.dto.solution.reqeust.UpdateSolutionRequest;
import com.prograngers.backend.dto.solution.response.*;
import com.prograngers.backend.entity.comment.Comment;
import com.prograngers.backend.entity.member.Member;
import com.prograngers.backend.entity.problem.Problem;
import com.prograngers.backend.entity.review.Review;
import com.prograngers.backend.entity.solution.AlgorithmConstant;
import com.prograngers.backend.entity.solution.DataStructureConstant;
import com.prograngers.backend.entity.solution.Solution;
import com.prograngers.backend.exception.badrequest.PrivateSolutionException;
import com.prograngers.backend.exception.unauthorization.MemberUnAuthorizedException;
import com.prograngers.backend.repository.comment.CommentRepository;
import com.prograngers.backend.repository.likes.LikesRepository;
import com.prograngers.backend.repository.member.MemberRepository;
import com.prograngers.backend.repository.review.ReviewRepository;
import com.prograngers.backend.repository.solution.SolutionRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.prograngers.backend.entity.solution.AlgorithmConstant.BFS;
import static com.prograngers.backend.entity.solution.AlgorithmConstant.DFS;
import static com.prograngers.backend.entity.solution.DataStructureConstant.ARRAY;
import static com.prograngers.backend.entity.solution.DataStructureConstant.LIST;
import static com.prograngers.backend.entity.solution.DataStructureConstant.QUEUE;
import static com.prograngers.backend.entity.solution.LanguageConstant.JAVA;
import static com.prograngers.backend.support.fixture.CommentFixture.생성된_댓글;
import static com.prograngers.backend.support.fixture.MemberFixture.장지담;
import static com.prograngers.backend.support.fixture.ProblemFixture.백준_문제;
import static com.prograngers.backend.support.fixture.ReviewFixture.FIRST_LINE_REVIEW;
import static com.prograngers.backend.support.fixture.ReviewFixture.SECOND_LINE_REVIEW;
import static com.prograngers.backend.support.fixture.SolutionFixture.공개_풀이;
import static com.prograngers.backend.support.fixture.SolutionFixture.비공개_풀이;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class SolutionServiceTest {
    @Mock
    private SolutionRepository solutionRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private LikesRepository likesRepository;
    @InjectMocks
    private SolutionService solutionService;

    @DisplayName("스크랩 해서 풀이를 저장할 수 있다")
    @Test
    void 스크랩_저장_테스트() {
        // given
        Member member = 장지담.아이디_지정_생성(1L);
        Problem problem = 백준_문제.기본_정보_생성();
        // 스크랩 당할 풀이 scrapTarget
        Solution scrapTarget = 공개_풀이.아이디_지정_생성(1L, problem, member, LocalDateTime.now(), DFS, LIST, JAVA, 1);

        // 스크랩 요청 생성
        ScarpSolutionRequest request = 스크랩_풀이_생성_요청_생성("풀이제목", "풀이설명", 5);

        // 스크랩의 결과로 만들어져야 하는 풀이
        Solution scrapResult = request.toSolution(scrapTarget, member);

        when(solutionRepository.findById(any())).thenReturn(Optional.of(scrapTarget));
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(solutionRepository.save(any())).thenReturn(Optional.of(scrapResult).get());

        // when
        solutionService.saveScrap(scrapTarget.getId(), request, member.getId());

        // then
        verify(solutionRepository, times(1)).save(any());
    }

    @DisplayName("내 풀이가 아닌 비공개 풀이를 조회하면 예외가 발생한다")
    @Test
    void 비공개_풀이_조회_예외_발생() {
        // given
        Member member1 = 장지담.아이디_지정_생성(1L);
        Member member2 = 장지담.아이디_지정_생성(2L);
        Problem problem = 백준_문제.기본_정보_생성();
        Solution solution = 비공개_풀이.기본_정보_생성(problem, member1, LocalDateTime.now(), BFS, QUEUE, JAVA, 1);

        // when
        when(solutionRepository.findById(solution.getId())).thenReturn(Optional.ofNullable(solution));
        when(memberRepository.findById(member2.getId())).thenReturn(Optional.ofNullable(member2));

        // then
        assertThrows(PrivateSolutionException.class, () -> solutionService.getSolutionDetail(solution.getId(), member2.getId()));
    }

    @DisplayName("내 풀이가 아닌 풀이를 수정하려고 하면 예외가 발생한다")
    @Test
    void 내_풀이_아닌_풀이_수정_시_예외_발생() {
        // given
        Member member1 = 장지담.아이디_지정_생성(1L);
        Member member2 = 장지담.아이디_지정_생성(2L);
        Problem problem = 백준_문제.기본_정보_생성();
        Solution solution1 = 공개_풀이.아이디_지정_생성(1L, problem, member1, LocalDateTime.now(), BFS, QUEUE, JAVA, 1);

        UpdateSolutionRequest request = 풀이_수정_요청_생성("수정제목", BFS, ARRAY, "수정코드", "수정설명", 1);

        when(memberRepository.findById(any())).thenReturn(Optional.of(member2));
        when(solutionRepository.findById(any())).thenReturn(Optional.of(solution1));

        // when , then
        assertThrows(
                MemberUnAuthorizedException.class,
                () -> solutionService.update(solution1.getId(), request, member2.getId()
                ));
    }

    @DisplayName("내 풀이가 아닌 풀이를 삭제하려고 하면 예외가 발생한다")
    @Test
    void 내_풀이_아닌_풀이_삭제_시_예외_발생() {
        // given
        Member member1 = 장지담.아이디_지정_생성(1L);
        Member member2 = 장지담.아이디_지정_생성(2L);
        Problem problem = 백준_문제.기본_정보_생성();
        Solution solution1 = 공개_풀이.아이디_지정_생성(1L, problem, member1, LocalDateTime.now(), BFS, QUEUE, JAVA, 1);

        when(memberRepository.findById(any())).thenReturn(Optional.of(member2));
        when(solutionRepository.findById(any())).thenReturn(Optional.of(solution1));

        // when , then
        assertThrows(
                MemberUnAuthorizedException.class,
                () -> solutionService.delete(solution1.getId(), member2.getId()
                ));
    }

    @DisplayName("내 풀이 상세보기가 가능하다")
    @Test
    void getMySolutionDetailTest(){

        //given
        final Long member1Id = 1L;
        final Long member2Id = 2L;
        final Long solutionId = 1L;

        //member
        final Member member1 = 장지담.아이디_지정_생성(member1Id);
        final Member member2 = 장지담.아이디_지정_생성(member2Id);

        //problem
        final Problem problem = 백준_문제.기본_정보_생성();

        //mainSolution
        final Solution myMainSolution = 공개_풀이.아이디_지정_생성(1L,problem, member1, LocalDateTime.now().plusDays(2), JAVA, 3);

        //이 문제에 대한 다른 사람들의 풀이 2개
        final Solution othersSolution1 = 공개_풀이.아이디_지정_생성(2L,problem, member2, LocalDateTime.now(), JAVA, 3);
        final Solution othersSolution2 = 공개_풀이.아이디_지정_생성(3L,problem, member2, LocalDateTime.now().plusDays(1), JAVA, 3);

        //이 문제에 대한 내풀이 3개, 하나는 다른 사람의 풀이를 스크랩한 풀이다
        final Solution mySolution1 = 공개_풀이.아이디_지정_생성(4L,problem, member1, LocalDateTime.now().plusDays(3), JAVA, 3);
        final Solution mySolution2 = 공개_풀이.아이디_지정_생성(5L,problem, member1, LocalDateTime.now().plusDays(4), JAVA, 3);
        final Solution mySolution3 = 공개_풀이.스크랩_아아디_지정_생성(6L,member1, LocalDateTime.now().plusDays(5),3,othersSolution1);

        //myMainSolution 댓글
        final Comment comment1 = 생성된_댓글.아이디_지정_생성(1L,member1, myMainSolution, LocalDateTime.now().plusDays(10));
        final Comment comment2 = 생성된_댓글.부모_지정_생성(1L, 2L, member2, myMainSolution, LocalDateTime.now().plusDays(11));
        final Comment comment3 = 생성된_댓글.아이디_지정_생성(3L,member2, myMainSolution, LocalDateTime.now().plusDays(12));
        final Comment comment4 = 생성된_댓글.부모_지정_생성(3L, 4L, member1, myMainSolution, LocalDateTime.now().plusDays(13));

        //myMainSolution 리뷰
        final Review review1 = FIRST_LINE_REVIEW.아이디_지정_생성(1L, member1, myMainSolution, LocalDateTime.now().plusDays(10));
        final Review review2 = FIRST_LINE_REVIEW.부모_지정_생성(1L,2L,member2,myMainSolution,LocalDateTime.now().plusDays(11));
        final Review review3 = SECOND_LINE_REVIEW.아이디_지정_생성(3L,member1,myMainSolution,LocalDateTime.now().plusDays(10));
        final Review review4 = SECOND_LINE_REVIEW.부모_지정_생성(3L,4L,member2,myMainSolution,LocalDateTime.now().plusDays(11));

        when(solutionRepository.findById(solutionId)).thenReturn(Optional.of(myMainSolution));
        when(solutionRepository.findAllByProblemOrderByCreatedAtAsc(problem)).thenReturn(Arrays.asList(mySolution3,mySolution2,mySolution1,myMainSolution,othersSolution2,othersSolution1));
        when(likesRepository.countBySolution(myMainSolution)).thenReturn(3L);
        when(solutionRepository.countByScrapSolution(myMainSolution)).thenReturn(2L);
        when(commentRepository.findAllBySolutionOrderByCreatedAtAsc(myMainSolution)).thenReturn(Arrays.asList(comment1,comment2,comment3,comment4));
        when(reviewRepository.findAllBySolutionOrderByCodeLineNumberAsc(myMainSolution)).thenReturn(Arrays.asList(review1,review2,review3,review4));
        when(solutionRepository.findTopLimitsSolutionOfProblemOrderByLikesDesc(problem,6)).thenReturn(Arrays.asList(othersSolution1,othersSolution2));

        final List<CommentWithRepliesResponse> expectedComments = Arrays.asList(CommentWithRepliesResponse.of(comment1, new ArrayList<>(), true), CommentWithRepliesResponse.of(comment3, new ArrayList<>(), false));
        expectedComments.get(0).getReplies().add(CommentWithRepliesResponse.of(comment2,false));
        expectedComments.get(1).getReplies().add(CommentWithRepliesResponse.of(comment4,true));

        final List<ReviewWithRepliesResponse> expectedReviews = Arrays.asList(ReviewWithRepliesResponse.from(review1, new ArrayList<>(), true), ReviewWithRepliesResponse.from(review3, new ArrayList<>(), true));
        expectedReviews.get(0).getReplies().add(ReviewWithRepliesResponse.from(review2,false));
        expectedReviews.get(1).getReplies().add(ReviewWithRepliesResponse.from(review4,false));

        final List<RecommendedSolutionResponse> expectedRecommendedSolutions = Arrays.asList(
                RecommendedSolutionResponse.from(othersSolution1.getId(), 0, othersSolution1.getTitle(), othersSolution1.getMember().getNickname()),
                RecommendedSolutionResponse.from(othersSolution2.getId(), 0, othersSolution2.getTitle(), othersSolution2.getMember().getNickname())
        );

        final List<SolutionTitleAndIdResponse> expectedSideSolutions = Arrays.asList(
                SolutionTitleAndIdResponse.from(mySolution3.getTitle(), mySolution3.getId()),
                SolutionTitleAndIdResponse.from(mySolution2.getTitle(), mySolution2.getId()),
                SolutionTitleAndIdResponse.from(mySolution1.getTitle(), mySolution1.getId()),
                SolutionTitleAndIdResponse.from(myMainSolution.getTitle(), myMainSolution.getId())
        );

        final List<SolutionTitleAndIdResponse> expectedScrapSolutions = Arrays.asList(SolutionTitleAndIdResponse.from(othersSolution1.getTitle(), othersSolution1.getId()));

        //when
        final ShowMySolutionDetailResponse expected = ShowMySolutionDetailResponse.of(
                ProblemResponse.from(problem.getTitle(),problem.getOjName()),
                MySolutionResponse.from(myMainSolution.getTitle(),Arrays.asList(myMainSolution.getAlgorithm(),myMainSolution.getDataStructure()),myMainSolution.getDescription(),myMainSolution.getCode().split("\n"),3L,2L),
                expectedComments,
                expectedReviews,
                expectedRecommendedSolutions,
                expectedSideSolutions,
                expectedScrapSolutions
        );
        final ShowMySolutionDetailResponse result = solutionService.getMySolutionDetail(member1Id, solutionId);

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    ScarpSolutionRequest 스크랩_풀이_생성_요청_생성(String title, String description, int level) {
        return new ScarpSolutionRequest(title, description, level);
    }

    private static UpdateSolutionRequest 풀이_수정_요청_생성(
            String title, AlgorithmConstant algorithm, DataStructureConstant dataStructure,
            String code, String description, int level) {
        return new UpdateSolutionRequest(title, algorithm, dataStructure, code, description, level);
    }

}