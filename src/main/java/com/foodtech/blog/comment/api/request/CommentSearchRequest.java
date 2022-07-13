package com.foodtech.blog.comment.api.request;

import com.foodtech.blog.base.api.request.SearchRequest;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CommentSearchRequest extends SearchRequest {
    @ApiParam(name = "articleId", value = "Search by article", required = false)
    private ObjectId articleId;
    @ApiParam(name = "userId", value = "Search by user", required = false)
    private ObjectId userId;
}
