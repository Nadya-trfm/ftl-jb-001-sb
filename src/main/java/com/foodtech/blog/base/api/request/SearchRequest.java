package com.foodtech.blog.base.api.request;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {
    @ApiParam(name = "query", value = "Search by fields", required = false)
    protected String query = null;
    @ApiParam(name = "size", value = "List size(10)", required = false)
    protected Integer size = 100;
    @ApiParam(name = "skip", value = "Skip first search(def 0)", required = false)
    protected Long skip = Long.valueOf(0);
}
