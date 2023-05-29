package Teemo.Teemo_backend.controller;

import Teemo.Teemo_backend.domain.Tag;
import Teemo.Teemo_backend.domain.dtos.*;
import Teemo.Teemo_backend.error.InvalidRangeException;
import Teemo.Teemo_backend.error.InvalidStateException;
import Teemo.Teemo_backend.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagControllerImpl {
    private final TagService tagService;
    /**
     * 태그 생성
     *
     * @input   : memberId, latitude, longitude, title, detail, maxNum, targetGender, upperAge, lowerAge
     * @output  : 201 CREATED
     */
    @PostMapping("/upload")
    public ResponseEntity createTag(@Valid @RequestBody TagCreateRequest request){
        // service 계층 : 입력 데이터의 유효성 검사
        try {
            tagService.upload(request);
        }
        catch (InvalidRangeException e){
            return getMapResponseEntity("InvalidRangeException",e.getField());
        }catch (InvalidStateException e){
            return getMapResponseEntity("InvalidStateException",e.getField());
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 주변 태그 검색
     *
     * @input   : memberId, latitude, longitude
     * @output  : [ {tagId,latitude,longitude},{tagId,latitude,longitude},....,{tagId,latitude,longitude} ]
     */
    @GetMapping("/search")
    public List<TagSearchResponse> searchTags(
            @RequestParam("memberId") Long memberId,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude
    )
    {
        List<Tag> list = tagService.search(memberId,latitude,longitude);
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
    @GetMapping("/find/{tagId}")
    public TagFindResponse findTag(@PathVariable Long tagId)
    {
        return tagService.find(tagId);
    }


    /**
     * 특정 태그 구독
     *
     * @input   : memberId, tagId
     * @output  : tagId, latitude, longitude
     */
    @PostMapping("/subscribe")
    public TagSubscribeResponse subscribeTag(@RequestBody TagCommonRequest request){
        TagSubscribeResponse response = tagService.subscribe(request.getMemberId(),request.getTagId());
        return response;
    }

    /**
     * 특정 태그 구독 취소
     *
     * @input   : memberId, tagId
     * @output  : 200 OK
     */
    @PostMapping("/unsubscribe")
    public ResponseEntity unsubscribeTag(@RequestBody TagCommonRequest request){
        tagService.unsubscribe(request.getMemberId(),request.getTagId());
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 태그 삭제
     *
     * @input   : tagId
     * @output  : 204 No Content
     */
    @DeleteMapping("/delete")
    public ResponseEntity deleteTag(@RequestBody TagCommonRequest request){
        tagService.remove(request.getMemberId(),request.getTagId());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    private static ResponseEntity<Map<String, Object>> getMapResponseEntity(String error,String field) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", error);
        errorResponse.put("invalid field", field);
        return ResponseEntity.badRequest().body(errorResponse);
    }
}


