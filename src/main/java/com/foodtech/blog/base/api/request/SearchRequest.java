package com.foodtech.blog.base.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {
    protected String query = null;
    protected Integer size = 100;
    protected Long skip = Long.valueOf(0);
}
