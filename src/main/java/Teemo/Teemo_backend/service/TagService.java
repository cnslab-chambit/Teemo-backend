package Teemo.Teemo_backend.service;

import Teemo.Teemo_backend.domain.Tag;
import Teemo.Teemo_backend.domain.dtos.*;

import java.util.List;

public interface TagService {
    void upload(TagCreateRequest request); // 태그 업로드
    List<Tag> search(Long memberId, Double latitude, Double longitude); // 주변 태그 검색
    TagFindResponse find(Long tagId); // 특정 태그 정보 검색
    TagSubscribeResponse subscribe(Long memberId, Long tagId); // 특정 태그 구독
    void unsubscribe(Long memberId, Long tagId);// 특정 태그 구독 취소
    void remove(Long memberId, Long tagId); // 태그 삭제
}
