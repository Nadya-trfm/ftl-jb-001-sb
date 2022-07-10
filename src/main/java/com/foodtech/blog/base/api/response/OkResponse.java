package com.foodtech.blog.base.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OkResponse <T>{
    public enum Status{
        SUCCESS,ERROR
    }
    protected T result;
    protected Status status;

    public static <T> OkResponse of(T t){
        OkResponse okResponse = new OkResponse();
        okResponse.setResult(t);
        okResponse.setStatus(Status.SUCCESS);
        return okResponse;
    }

}
