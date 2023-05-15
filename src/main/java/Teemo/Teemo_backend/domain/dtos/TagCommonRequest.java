package Teemo.Teemo_backend.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TagCommonRequest {
    private Long memberId;
    private Long tagId;
}
