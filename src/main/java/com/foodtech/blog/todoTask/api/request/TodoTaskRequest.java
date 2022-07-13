package com.foodtech.blog.todoTask.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
@ApiModel(value ="TodoTaskRequest",description = "Model for update TodoTask")
public class TodoTaskRequest {
            private ObjectId id;
            private String title;
            private ObjectId ownerId;
            private Boolean completed;
            private List<ObjectId> files;
}
