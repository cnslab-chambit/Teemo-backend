package Teemo.Teemo_backend.controller;

import Teemo.Teemo_backend.domain.Tag;
import Teemo.Teemo_backend.domain.dtos.TagCreateRequest;
import Teemo.Teemo_backend.domain.dtos.TagFindResponse;
import Teemo.Teemo_backend.domain.dtos.TagSearchResponse;
import Teemo.Teemo_backend.domain.dtos.TagSubscribeResponse;
import Teemo.Teemo_backend.repository.TagRepository;
import Teemo.Teemo_backend.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    /**
     * 태그 생성
     *
     * @input   : userId, latitude, longitude, title, detail, maxNum, targetGender, upperAge, lowerAge
     * @output  : 201 CREATED
     */
    @PostMapping("/upload")
    public ResponseEntity createTag(@RequestBody TagCreateRequest request){
        tagService.upload(request);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 주변 태그 검색
     *
     * @input   : userId, latitude, longitude
     * @output  : [ {tagId,latitude,longitude},{tagId,latitude,longitude},....,{tagId,latitude,longitude} ]
     */
    @GetMapping("/find/{userId}/{latitude}/{longitude}")
    public List<TagSearchResponse> searchTags(
            @PathVariable Long userId,
            @PathVariable Double latitude,
            @PathVariable Double longitude
    )
    {
        List<Tag> list = tagService.search(userId,latitude,longitude);
        List<TagSearchResponse> responses = new ArrayList<>();
        for(Tag tag: list){
            responses.add(new TagSearchResponse(tag.getId(),tag.getLatitude(),tag.getLongitude()));
        }
        return responses;
    }

    /**
     * 특정 태그 정보 검색
     *
     * @input   : tagId
     * @output  : tagId, title, maxNum, targetGender, upperAge, lowerAge, remainingTime, hostNickName, hostGender, hostAge
     */
    @GetMapping("/search/{tagId}")
    public TagFindResponse findTag(@PathVariable Long tagId)
    {
        return tagService.find(tagId);
    }


    /**
     * 특정 태그 구독
     *
     * @input   : userId, tagId
     * @output  : tagId, latitude, longitude
     */
    @PostMapping("/subscribe/{userId}/{tagId}")
    public TagSubscribeResponse subscribeTag(
            @PathVariable("userId")Long userId,
            @PathVariable("tagId") Long tagId
    ){
        TagSubscribeResponse response = tagService.subscribe(userId, tagId);
        return response;
    }

    /**
     * 특정 태그 구독 취소
     *
     * @input   : tagId, userId
     * @output  : 200 OK
     */
    @PostMapping("/unsubscribe/{userId}/{tagId}")
    public ResponseEntity unsubscribeTag(
            @PathVariable("userId") Long userId, @PathVariable("tagId") Long tagId
    ){
        tagService.unsubscribe(userId,tagId);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 태그 삭제
     *
     * @input   : tagId
     * @output  : 204 No Content
     */
    @DeleteMapping("/delete/{userId}/{tagId}")
    public ResponseEntity deleteTag(
            @PathVariable("userId") Long userId,
            @PathVariable("tagId") Long tagId){
        tagService.remove(userId,tagId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
