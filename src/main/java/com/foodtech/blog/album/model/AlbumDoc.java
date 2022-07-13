package com.foodtech.blog.album.model;

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
public class AlbumDoc {
    @Id
     private ObjectId id;
     private String title;
     private ObjectId ownerId;
}
