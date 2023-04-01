package api.domain.dtos;

import api.domain.Tag;

public class FindTagsResponse {
    private double latitude; // 위도
    private double longitude; // 경도

    public FindTagsResponse(Tag tag) {
        this.latitude = tag.getLatitude();
        this.longitude = tag.getLongitude();
    }
}
