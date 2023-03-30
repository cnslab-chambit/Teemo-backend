package api.domain.dtos;

import lombok.Data;
import lombok.Getter;

@Getter
public class GotoTagRequest {
    private Long tagId;
    private Long memberId;
}
