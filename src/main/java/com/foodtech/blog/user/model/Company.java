package com.foodtech.blog.user.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    private String name;
    private Address address;
}
