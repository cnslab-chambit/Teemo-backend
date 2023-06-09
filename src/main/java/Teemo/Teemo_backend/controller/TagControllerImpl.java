package Teemo.Teemo_backend.controller;

import Teemo.Teemo_backend.domain.Tag;
import Teemo.Teemo_backend.domain.dtos.*;
import Teemo.Teemo_backend.error.CustomErrorResponse;
import Teemo.Teemo_backend.error.CustomInvalidValueException;
import Teemo.Teemo_backend.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.info("태그 생성");
        // service 계층 : 입력 데이터의 유효성 검사
        try {
            tagService.upload(request);
        }
        catch (CustomInvalidValueException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(e.getField(), e.getMessage());log.info("비정상 응답");
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }log.info("정상 응답");
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 주변 태그 검색
     *
     * @input   : memberId, latitude, longitude
     * @output  : [ {tagId,latitude,longitude},{tagId,latitude,longitude},....,{tagId,latitude,longitude} ]
     */
    @GetMapping("/search")
    public ResponseEntity<List<TagSearchResponse>> searchTags(
            @RequestParam("memberId") Long memberId,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude
    )
    {
        log.info("주변 태그 검색");
        List<Tag> list = null;
        try {
            list = tagService.search(memberId, latitude, longitude);
        }catch (CustomInvalidValueException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(e.getField(), e.getMessage());
            log.info("비정상 응답");
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
        List<TagSearchResponse> responses = new ArrayList<>();
        for(Tag tag: list){
            responses.add(new TagSearchResponse(tag.getId(),tag.getLatitude(),tag.getLongitude()));
        }log.info("정상 응답");
        return ResponseEntity.ok(responses);
    }

    /**
     * 특정 태그 정보 검색
     *
     * @input   : tagId
     * @output  : tagId, title, maxNum, targetGender, upperAge, lowerAge, remainingTime, hostNickName, hostGender, hostAge
     */
    @GetMapping("/find/{tagId}")
    public ResponseEntity<TagFindResponse> findTag(@PathVariable Long tagId)
    {
        log.info("특정 태그 정보 검색");
        TagFindResponse response = null;
        try{
            response = tagService.find(tagId);
        }catch (CustomInvalidValueException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(e.getField(), e.getMessage());
            log.info("비정상 응답");
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }log.info("정상 응답");
        return ResponseEntity.ok(response);
    }


    /**
     * 특정 태그 구독
     *
     * @input   : memberId, tagId
     * @output  : tagId, latitude, longitude
     */
    @PostMapping("/subscribe")
    public ResponseEntity<TagSubscribeResponse> subscribeTag(@RequestBody TagCommonRequest request){
        log.info("특정 태그 구독");
        TagSubscribeResponse response = null;
        try {
            response = tagService.subscribe(request.getMemberId(), request.getTagId());
        }catch (CustomInvalidValueException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(e.getField(), e.getMessage());
            log.info("비정상 응답");
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }log.info("정상 응답");
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 태그 구독 취소
     *
     * @input   : memberId, tagId
     * @output  : 200 OK
     */
    @PostMapping("/unsubscribe")
    public ResponseEntity unsubscribeTag(@RequestBody TagCommonRequest request){
        log.info("특정 태그 구독 취소");
        try {
            tagService.unsubscribe(request.getMemberId(), request.getTagId());
        }catch (CustomInvalidValueException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(e.getField(), e.getMessage());
            log.info("비정상 응답");
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }log.info("정상 응답");
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
        log.info("태그 삭제");
        try {
            tagService.remove(request.getMemberId(), request.getTagId());
        }catch (CustomInvalidValueException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(e.getField(), e.getMessage());
            log.info("비정상 응답");
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
        log.info("정상 응답");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}


