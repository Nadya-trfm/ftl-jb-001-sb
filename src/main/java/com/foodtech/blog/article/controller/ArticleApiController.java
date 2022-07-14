package com.foodtech.blog.article.controller;

import com.foodtech.blog.auth.exceptions.AuthException;
import com.foodtech.blog.auth.exceptions.NotAccessException;
import com.foodtech.blog.base.api.request.SearchRequest;
import com.foodtech.blog.base.api.response.OkResponse;
import com.foodtech.blog.base.api.response.SearchResponse;
import com.foodtech.blog.article.api.request.ArticleRequest;
import com.foodtech.blog.article.api.response.ArticleResponse;
import com.foodtech.blog.article.exeception.ArticleExistException;
import com.foodtech.blog.article.exeception.ArticleNotExistException;
import com.foodtech.blog.article.mapping.ArticleMapping;
import com.foodtech.blog.article.routes.ArticleApiRoutes;
import com.foodtech.blog.article.service.ArticleApiService;
import com.foodtech.blog.user.exeception.UserNotExistException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "Article api")
public class ArticleApiController {
    private final ArticleApiService articleApiService;

    @GetMapping(ArticleApiRoutes.BY_ID)
    @ApiOperation(value = "find Article by id",notes = "use this if you need full information by Article")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 404,message = "Article not found"),
    })
    public OkResponse<ArticleResponse> byId( @ApiParam(value = "Article id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
        return  OkResponse.of(ArticleMapping.getInstance().getResponse().convert(
                articleApiService.findByID(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
        ));
    }

    @PostMapping(ArticleApiRoutes.ROOT)
    @ApiOperation(value = "Create",notes="use this when you need create new Article")
    @ApiResponses(value = {
           @ApiResponse(code = 200,message = "Success"),
           @ApiResponse(code = 400,message = "Article already exist")
    })
    public OkResponse<ArticleResponse> create(@RequestBody ArticleRequest request) throws ArticleExistException, UserNotExistException, AuthException {
        return OkResponse.of(ArticleMapping.getInstance().getResponse().convert(articleApiService.create(request)));
    }

    @GetMapping(ArticleApiRoutes.ROOT)
    @ApiOperation(value = "search Article",notes = "use this if you need find Article by title or body")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<SearchResponse<ArticleResponse>> search(
            @ModelAttribute SearchRequest request
            ){
        return  OkResponse.of(ArticleMapping.getInstance().getSearch().convert(
                articleApiService.search(request)
        ));
    }

    @PutMapping(ArticleApiRoutes.BY_ID)
    @ApiOperation(value = "update Article",notes = "use this if you need update Article")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400,message = "Article ID invalid"),
    })
    public OkResponse<ArticleResponse> updateById(
            @ApiParam(value = "Article id") @PathVariable String id,
            @RequestBody ArticleRequest articleRequest
            ) throws ArticleNotExistException, NotAccessException, AuthException {
        return OkResponse.of(ArticleMapping.getInstance().getResponse().convert(
                articleApiService.update(articleRequest)
        ));
    }

    @DeleteMapping(ArticleApiRoutes.BY_ID)
    @ApiOperation(value = "delete Article",notes = "use this if you need delete Article")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<String> deleteById(@ApiParam(value = "Article id") @PathVariable ObjectId id) throws NotAccessException, AuthException, ChangeSetPersister.NotFoundException {
        articleApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
