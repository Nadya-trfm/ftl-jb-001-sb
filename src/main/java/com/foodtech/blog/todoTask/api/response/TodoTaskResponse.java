package com.foodtech.blog.todoTask.api.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ApiModel(value ="TodoTaskResponse",description = "TodoTask data(for search and list)")
public class TodoTaskResponse {
            protected String id;
            protected String title;
            protected String ownerId;
            protected Boolean completed;
            protected List<String> files;
}
