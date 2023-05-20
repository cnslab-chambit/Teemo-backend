package Teemo.Teemo_backend.domain.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TagCommonRequest {
    @NotNull
    private Long memberId;
    @NotNull
    private Long tagId;
}
