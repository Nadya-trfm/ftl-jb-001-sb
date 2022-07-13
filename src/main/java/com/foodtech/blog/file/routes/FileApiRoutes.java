package com.foodtech.blog.file.routes;

import com.foodtech.blog.base.routers.BaseApiRoutes;

public class FileApiRoutes {
    public static final String ROOT = BaseApiRoutes.V1 +"/file";
    public static final String BY_ID = ROOT+"/{id}";
    public static final String DOWNLOAD = "/files/{id}";
}
