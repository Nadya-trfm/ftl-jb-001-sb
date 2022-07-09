package com.foodtech.blog.user.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String city;
    private String street;
    private String suite;
    private String zipcode;
    private Point point;
}
