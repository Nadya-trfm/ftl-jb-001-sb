package com.foodtech.blog.article.routes;

import com.foodtech.blog.base.routers.BaseApiRoutes;

public class ArticleApiRoutes {
    public static final String ROOT = BaseApiRoutes.V1 +"/article";
    public static final String BY_ID = ROOT+"/{id}";
}
