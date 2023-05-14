package Teemo.Teemo_backend.repository;

import Teemo.Teemo_backend.domain.Gender;
import Teemo.Teemo_backend.domain.Tag;

import java.util.List;

public interface TagRepository {
    void save(Tag tag); // 저장
    Tag findById(Long tagId); // id 로 검색
    List<Tag> findByCriteria(Double latitude, Double longitude, Integer age, Gender gender); // 조건에 맞춰 불러오기
    Long delete(Tag tag); // 삭제
}
