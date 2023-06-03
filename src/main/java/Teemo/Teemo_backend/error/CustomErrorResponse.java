package Teemo.Teemo_backend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomErrorResponse {
    private String field;
    private String message;
}