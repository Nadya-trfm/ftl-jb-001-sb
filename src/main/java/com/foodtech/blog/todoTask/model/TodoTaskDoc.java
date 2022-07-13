package com.foodtech.blog.todoTask.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoTaskDoc {
    @Id
     private ObjectId id;
     private String title;
     private ObjectId ownerId;
     private Boolean completed;
     private List<ObjectId> files;
}
