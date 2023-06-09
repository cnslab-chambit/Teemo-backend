package Teemo.Teemo_backend.domain.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static Teemo.Teemo_backend.util.DateTimeParse.applyTimeExpression;

@NoArgsConstructor
@Getter
public class TagCreateResponse {
    private Long tagId; //DB 탐색용 식별자
    private String createdAt; // tag 생성 시간
    private String deletedAt; // tag 삭제 시간
    public TagCreateResponse(Long tagId, LocalDateTime createdAt, LocalDateTime deletedAt) {
        this.tagId = tagId;
        StringBuilder sb = new StringBuilder();
        this.createdAt = applyTimeExpression(createdAt);
        this.deletedAt = applyTimeExpression(deletedAt);
    }
}
