package api.domain.dtos;

import api.domain.Tag;
import lombok.Data;

@Data
public class SubscribeTagResponse {
    private Long tagId; // 태그아이디
    private double latitude; // tag 의 위도
    private double longitude; // tag 의 경도

    public SubscribeTagResponse(Tag tag){
        this.tagId = tag.getId();
        this.latitude = tag.getLatitude();
        this.longitude = tag.getLongitude();
    }
}
