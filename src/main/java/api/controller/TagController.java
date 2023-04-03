package api.controller;

import api.domain.Tag;
import api.domain.dtos.*;
import api.repository.TagRepository;
import api.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TagController {
    private final TagRepository tagRepository;
    private final TagService tagService;

    /** [Host]
     * 태그 생성
     */
    @PostMapping("/tags")
    public ResponseEntity createTag(@RequestBody CreateTagRequest request){
        tagService.uploadTag(request);
        // 성공시, 201 상태코드 전달
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * tag 전체 조회
     *
     * tag 의 위도, 경도만 반환한다.
     */
    @GetMapping("/tags")
    public List<FindTagsResponse> findTags(@RequestBody FindTagsRequest request){
        return tagService.findTags(request.getMemberId(),request.getLatitude(),request.getLongitude());
    }

    /**
     * 특정 tag 조회
     */
    @GetMapping("/tags/{tagId}")
    public SearchTagResponse searchTag(@PathVariable("tagId")Long tagId)
    {
        return tagService.searchTag(tagId);
    }

    /** [Guest]
     * 특정 tag 를 목적지로 설정
     *
     * - 조회자에게 Tag 의 위도와 경도를 제공.
     * - 조회자 멤버변수에 Tag 등록
     * - 조회자의 역할을 GUEST 로 바꿔야한다.
     */
    @PostMapping("/tags/{tagId}/subscribe")
    public SubscribeTagResponse subscribeTag(
            @PathVariable("tagId")Long tagId,
            @RequestParam("memberId") Long memberId )
    {
        log.info("tagId={}",tagId);
        log.info("memberId={}",memberId);
        return tagService.subscribeTag(tagId, memberId);
    }

    /** [Guest]
     * 목적지로 설정한 tag 를 포기
     */
    @PostMapping("/tags/{tagId}/unsubscribe")
    public ResponseEntity unsubscribeTag(
            @PathVariable("tagId")Long tagId,
            @RequestParam("memberId") Long memberId )
    {
        tagService.unsubscribeTag(tagId,memberId);
        return new ResponseEntity(HttpStatus.OK);
    }

    /** [Host]
     * tag 삭제
     */
    @DeleteMapping("/tags/{tagId}")
    public ResponseEntity deleteTag(@PathVariable("tagId")Long tagId){
        tagService.deleteTag(tagId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
