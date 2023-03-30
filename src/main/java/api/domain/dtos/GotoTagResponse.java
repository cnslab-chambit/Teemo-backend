package api.domain.dtos;

import api.domain.Tag;

public class GotoTagResponse {
    private double latitude; // tag 의 위도
    private double longitude; // tag 의 경도

    public GotoTagResponse(Tag tag){
        this.latitude = tag.getLatitude();
        this.longitude = tag.getLongitude();
    }
}
