package com.foodtech.blog.user.api.request;

import com.foodtech.blog.user.model.Address;
import com.foodtech.blog.user.model.Company;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ApiModel(value ="UserRequest",description = "Model for update user")
public class UserRequest {
    private ObjectId id;
    protected String firstName;
    protected String lastName;
    protected String email;
    private Address address = new Address();
    private Company company = new Company();
}
