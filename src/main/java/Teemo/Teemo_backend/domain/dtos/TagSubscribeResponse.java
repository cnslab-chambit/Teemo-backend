package Teemo.Teemo_backend.domain.dtos;

public class TagSubscribeResponse {
    Long tagId;
    Double latitude;
    Double longitude;

    public TagSubscribeResponse(
        Long tagId,
        Double latitude,
        Double longitude
    ){
        this.tagId = tagId;
        this.latitude = latitude;
        this.longitude=longitude;
    }
}
