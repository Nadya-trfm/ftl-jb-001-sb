package com.foodtech.blog.base.routers;

public class ExampleRoutes {
    public static final String GET = BaseApiRoutes.V1 + "/get";
    public static final String GET_WITH_PARAM = BaseApiRoutes.V1 + "/get-with-param";
    public static final String GET_WITH_PATH = BaseApiRoutes.V1 + "/get-with-path-{pathVariable}";
    public static final String PUT = BaseApiRoutes.V1 + "/put/{id}";
    public static final String POST = BaseApiRoutes.V1 + "/post";
    public static final String DELETE = BaseApiRoutes.V1 + "/delete/{id}";
}
