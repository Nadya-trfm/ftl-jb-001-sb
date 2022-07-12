package com.foodtech.blog.comment.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDoc {
    @Id
     private ObjectId id;
     private ObjectId articleId;
     private ObjectId userId;
     private String message;
}
