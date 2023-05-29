package Teemo.Teemo_backend.error;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomErrorResponse {
    private String field;
    private String message;
}