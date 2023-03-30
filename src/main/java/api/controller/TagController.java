package api.controller;

import api.domain.Tag;
import api.domain.dtos.CreateTagRequest;
import api.domain.dtos.FindTagsRequest;
import api.domain.dtos.FindTagsResponse;
import api.domain.dtos.SearchTagResponse;
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

    /**
     * tag 생성
     */
    @PostMapping("/tag/add")
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
    @GetMapping("/tag/{tag_id}")
    public SearchTagResponse searchTag(@PathVariable("tag_id")Long tagId){
        return tagService.searchTag(tagId);
    }

    /**
     * 특정 tag 를 목적지로 설정
     */

    /**
     * 목적지로 설정한 tag 를 포기
     */

    /**
     * tag 삭제
     */


}
