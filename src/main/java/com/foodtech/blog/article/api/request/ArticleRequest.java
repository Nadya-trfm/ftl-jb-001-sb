package com.foodtech.blog.article.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ApiModel(value ="ArticleRequest",description = "Model for update Article")
public class ArticleRequest {
            private ObjectId id;
            private String title;
            private String body;
}
