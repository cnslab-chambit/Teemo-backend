package api.domain.dtos;

import api.domain.Gender;

public class SearchTagResponse {
    private String title;
    private String detail;
    private int limit;

    private Gender gender; // host 의 성별
    private int age; // host 의 나이
}
