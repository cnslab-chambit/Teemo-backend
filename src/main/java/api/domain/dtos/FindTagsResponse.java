package api.domain.dtos;

import api.domain.Tag;
import lombok.Data;
import lombok.Getter;

@Getter
public class FindTagsResponse {
    private Long tagId; // 태그아이디
    private double latitude; // 위도
    private double longitude; // 경도

    public FindTagsResponse(Tag tag) {
        this.tagId = tag.getId();
        this.latitude = tag.getLatitude();
        this.longitude = tag.getLongitude();
    }
}
