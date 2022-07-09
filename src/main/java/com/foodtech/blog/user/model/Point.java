package com.foodtech.blog.user.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Point {
    private Double lat;
    private Double lng;
}
