package com.emmanuela.fintechapplication.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    private HttpStatus status;
    private String message;
    private T result;
}
