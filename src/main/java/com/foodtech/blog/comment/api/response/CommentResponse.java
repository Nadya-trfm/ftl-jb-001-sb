package com.foodtech.blog.comment.api.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ApiModel(value ="CommentResponse",description = "Comment data(for search and list)")
public class CommentResponse {
            protected String id;
            protected String articleId;
            protected String userId;
            protected String message;
}
