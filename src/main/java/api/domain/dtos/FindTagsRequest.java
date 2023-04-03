package api.domain.dtos;

import api.domain.Gender;
import lombok.Data;

@Data
public class FindTagsRequest {
    private Long memberId; // 조회 요청한 사람의 ID
    private double latitude; // 조회 요청한 사람의 위도
    private double longitude; // 조회 요청한 사람의 경도
}
