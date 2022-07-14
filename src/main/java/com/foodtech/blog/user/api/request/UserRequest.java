package com.foodtech.blog.user.api.request;

import com.foodtech.blog.base.api.model.Address;
import com.foodtech.blog.base.api.model.Company;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value ="UserRequest",description = "Model for update user")
public class UserRequest {

    protected String firstName;
    protected String lastName;
    protected String email;
    private Address address = new Address();
    private Company company = new Company();
}
