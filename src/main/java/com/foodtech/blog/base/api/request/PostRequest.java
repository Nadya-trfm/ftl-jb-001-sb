package com.foodtech.blog.base.api.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@ToString
public class PostRequest {

    @Getter
    @Setter
    @ToString
    public static class Inner{
        private String param;
    }

    private String param;
    private Integer paramInt;
    private Double paramDouble;
    private Inner paramInner;
}
