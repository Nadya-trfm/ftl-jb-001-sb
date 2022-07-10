package com.foodtech.blog.user.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value ="RegistrationRequest",description = "Model for register")
public class RegistrationRequest {
    private String email;
    private String password;
}
