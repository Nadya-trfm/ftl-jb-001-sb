package com.foodtech.blog.todoTask.routes;

import com.foodtech.blog.base.routers.BaseApiRoutes;

public class TodoTaskApiRoutes {
    public static final String ROOT = BaseApiRoutes.V1 +"/todo-task";
    public static final String BY_ID = ROOT+"/{id}";
}
