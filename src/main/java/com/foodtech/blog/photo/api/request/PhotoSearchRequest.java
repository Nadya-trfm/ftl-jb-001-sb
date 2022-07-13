package com.foodtech.blog.photo.api.request;

import com.foodtech.blog.base.api.request.SearchRequest;
import io.swagger.annotations.ApiParam;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoSearchRequest extends SearchRequest {
    @ApiParam(name = "albumId", value = "Search by fields", required = false)
    private ObjectId albumId;
}
