package api.domain.dtos;

import api.domain.Tag;
import lombok.Data;

@Data
public class SubscribeTagResponse {
    private double latitude; // tag 의 위도
    private double longitude; // tag 의 경도

    public SubscribeTagResponse(Tag tag){
        this.latitude = tag.getLatitude();
        this.longitude = tag.getLongitude();
    }
}
