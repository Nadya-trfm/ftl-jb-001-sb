package com.foodtech.blog.file.api.response;

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
@ApiModel(value ="FileResponse",description = "File data(for search and list)")
public class FileResponse {
            protected String id;
            protected String title;
            protected String ownerId;
}
