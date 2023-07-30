package com.prograngers.backend.controller;

import com.prograngers.backend.dto.*;
import com.prograngers.backend.entity.Comment;
import com.prograngers.backend.entity.Review;
import com.prograngers.backend.entity.Solution;
import com.prograngers.backend.service.CommentService;
import com.prograngers.backend.service.SolutionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/solutions")
@Slf4j
public class SolutionController {
    private final SolutionService solutionService;
    private final CommentService commentService;

    // solution 쓰기
    @PostMapping("/new-form")
    public ResponseEntity<?> newForm(@RequestBody @Valid SolutionRequest solutionRequest) throws URISyntaxException {

        // Valid 확인 -> 검증 실패할 경우 MethodArgumentNotValidException

        // 리포지토리 활용해 저장
        Solution solution = solutionRequest.toEntity();
        Solution saved = solutionService.save(solution);
        Long solutionId = saved.getId();

        // 성공할 시 solutiuonId에 해당하는 URI로 리다이렉트, 상태코드 302
        URI redirectUri = new URI("http://localhost:8080/solutions/"+solutionId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping("/new-form/{scrapId}")
    public ResponseEntity<?> scrapForm(@PathVariable Long scrapId, @RequestBody ScarpSolutionRequest request)
            throws URISyntaxException {
        // 입력 폼과 스크랩 id로 새로운 Solution 생성
        Solution saved = solutionService.saveScrap(scrapId, request);

        // 성공할 시 solution 목록으로 리다이렉트, 상태코드 302
        URI redirectUri = new URI("http://localhost:8080/solutions/"+saved.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    // 수정 폼 반환
    @GetMapping("/{solutionId}/update-form")
    public ResponseEntity<?> updateForm(@PathVariable Long solutionId){
        Solution target = solutionService.findById(solutionId);
        return ResponseEntity.ok().body(SolutionUpdateForm.toDto(target));
    }

    // 수정 요청
    @PatchMapping("/{solutionId}")
    public ResponseEntity<?> update(@PathVariable Long solutionId,
                                    @RequestBody @Valid SolutionPatchRequest solutionPatchRequest) throws URISyntaxException {

        // solutionService로 update한다
        Solution updated = solutionService.update(solutionId,  solutionPatchRequest);

        // 성공할 시 solutiuonId에 해당하는 URI로 리다이렉트, 상태코드 302
        URI redirectUri = new URI("http://localhost:8080/solutions/"+updated.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    // 삭제 요청
    @DeleteMapping("/{solutionId}")
    public ResponseEntity<?> delete(@PathVariable Long solutionId) throws URISyntaxException {

        // solutionService로 delete한다
        solutionService.delete(solutionId);

        // 성공할 시 solution 목록으로 리다이렉트, 상태코드 302
        URI redirectUri = new URI("http://localhost:8080/solutions");
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);

    }

    // Solution 상세보기 요청
    @GetMapping("/{solutionId}")
    public ResponseEntity<?> solutionDetail(@PathVariable Long solutionId){
        Solution solution = solutionService.findById(solutionId);
        List<Comment> comments  = commentService.findBySolution(solution);
        SolutionDetailResponse solutionDetailResponse = SolutionDetailResponse.toEntity(solution,comments);
        return ResponseEntity.ok().body(solutionDetailResponse);
    }


    // 댓글 작성
    @PostMapping("/{solutionId}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long solutionId, @RequestBody CommentReqeust commentReqeust)
            throws URISyntaxException {

        Comment added = solutionService.addComment(solutionId, commentReqeust);

        // 성공할 시 solutiuonId에 해당하는 URI로 리다이렉트, 상태코드 302
        URI redirectUri = new URI("http://localhost:8080/solutions/"+added.getSolution().getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    // 댓글 수정
    @PatchMapping("comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId,
                                           @RequestBody CommentPatchRequest commentPatchRequest) throws URISyntaxException {

        Comment updated = commentService.updateComment(commentId);

        // 성공할 시 solutiuonId에 해당하는 URI로 리다이렉트, 상태코드 302
        URI redirectUri = new URI("http://localhost:8080/solutions/"+updated.getSolution().getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }


}
