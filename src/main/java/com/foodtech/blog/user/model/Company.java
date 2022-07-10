package com.foodtech.blog.user.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value ="Company",description = "company")
public class Company {
    private String name;
    private Address address;
}
