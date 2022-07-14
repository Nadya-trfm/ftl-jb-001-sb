package com.foodtech.blog.base.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@ApiModel(value ="Point",description = "coordinates")
public class Point {
    private Double lat;
    private Double lng;
}
