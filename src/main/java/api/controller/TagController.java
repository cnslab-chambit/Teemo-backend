package api.controller;

import api.domain.Tag;
import api.domain.dtos.*;
import api.repository.TagRepository;
import api.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagController {
    private final TagRepository tagRepository;
    private final TagService tagService;

    /** [Host]
     * 태그 생성
     */
    @PostMapping("/tags")
    public ResponseEntity createTag(@RequestBody CreateTagRequest request){
        // 1. 태그를 만든다.
        Tag tag = new Tag(
                request.getTitle(),
                request.getDetail(),
                request.getLimit(),
                request.getTargetGender(),
                request.getTargetAgeUpper(),
                request.getTargetAgeLower(),
                request.getLatitude(),
                request.getLongitude()
        );
        // 2. 서비스 계층에 member_id와 tag 를 넘긴다.
        tagService.uploadTag(request.getHostId(), tag);

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
        return tagService.findTags(request.getMemberID(),request.getLatitude(),request.getLongitude());
    }

    /**
     * 특정 tag 조회
     */
    @GetMapping("/tags/{tag_id}")
    public SearchTagResponse searchTag(@PathVariable("tag_id")Long tagId)
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
    @PostMapping("/tags/{tag_id}/subscribe")
    public SubscribeTagResponse subscribeTag(
            @PathVariable("tag_id")Long tagId,
            Long member_id )
    {
        return tagService.subscribeTag(tagId, member_id);
    }

    /** [Guest]
     * 목적지로 설정한 tag 를 포기
     */
    @PostMapping("/tags/{tag_id}/unsubscribe")
    public ResponseEntity unsubscribeTag(
            @PathVariable("tag_id")Long tagId,
            Long member_id )
    {
        tagService.unsubscribeTag(tagId,member_id);
        return new ResponseEntity(HttpStatus.OK);
    }

    /** [Host]
     * tag 삭제
     */
    @DeleteMapping("/tags/{tag_id}")
    public ResponseEntity deleteTag(@PathVariable("tag_id")Long tagId){
        tagService.deleteTag(tagId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
