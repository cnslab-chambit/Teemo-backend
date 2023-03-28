package api.controller;

import api.domain.Gender;
import api.domain.Tag;
import api.domain.dtos.CreateTagRequestDTO;
import api.repository.TagRepository;
import api.service.TagService;
import lombok.AllArgsConstructor;
import lombok.Data;
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
    public ResponseEntity createTag(@RequestBody CreateTagRequestDTO request){
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
     * 특정 tag 조회
     */
//    @GetMapping("/tag/{tag_id}")
//    public SerchTagResponseDTO searchTag(@PathVariable("tag_id")Long tagId){
//        Tag tag = tagRepository.find(tagId);
//
//    }

    /**
     * tag 전체 조회
     */
//    @GetMapping("/tags")
//    public List<FindTagResponse> findTags(){
//
//    }

    /**
     * tag 삭제
     */


}
