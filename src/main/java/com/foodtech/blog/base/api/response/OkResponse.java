package com.foodtech.blog.base.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "OkResponse",description = "Template for success response")
public class OkResponse <T>{
    public enum Status{
        SUCCESS,ERROR
    }
    @ApiModelProperty(value = "Response entity")
    protected T result;
    @ApiModelProperty(value = "Status")
    protected Status status;

    public static <T> OkResponse of(T t){
        OkResponse okResponse = new OkResponse();
        okResponse.setResult(t);
        okResponse.setStatus(Status.SUCCESS);
        return okResponse;
    }

}
