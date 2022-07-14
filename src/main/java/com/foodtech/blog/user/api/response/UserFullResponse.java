package com.foodtech.blog.user.api.response;

import com.foodtech.blog.base.api.model.Address;
import com.foodtech.blog.base.api.model.Company;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@ApiModel(value ="UserFullResponse",description = "user full data")
public class UserFullResponse extends  UserResponse{
    private Address address;
    private Company company;
}
