package com.foodtech.blog.comment.controller;

import com.foodtech.blog.article.exeception.ArticleNotExistException;
import com.foodtech.blog.auth.exceptions.AuthException;
import com.foodtech.blog.auth.exceptions.NotAccessException;
import com.foodtech.blog.base.api.request.SearchRequest;
import com.foodtech.blog.base.api.response.OkResponse;
import com.foodtech.blog.base.api.response.SearchResponse;
import com.foodtech.blog.comment.api.request.CommentRequest;
import com.foodtech.blog.comment.api.request.CommentSearchRequest;
import com.foodtech.blog.comment.api.response.CommentResponse;
import com.foodtech.blog.comment.exeception.CommentExistException;
import com.foodtech.blog.comment.exeception.CommentNotExistException;
import com.foodtech.blog.comment.mapping.CommentMapping;
import com.foodtech.blog.comment.routes.CommentApiRoutes;
import com.foodtech.blog.comment.service.CommentApiService;
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
@Api(value = "Comment api")
public class CommentApiController {
    private final CommentApiService commentApiService;

    @PostMapping(CommentApiRoutes.ROOT)
    @ApiOperation(value = "Create",notes="use this when you need create new Comment")
    @ApiResponses(value = {
           @ApiResponse(code = 200,message = "Success"),
           @ApiResponse(code = 400,message = "Comment already exist")
    })
    public OkResponse<CommentResponse> create(@RequestBody CommentRequest request) throws CommentExistException, ArticleNotExistException, UserNotExistException, AuthException {
        return OkResponse.of(CommentMapping.getInstance().getResponse().convert(commentApiService.create(request)));
    }

    @GetMapping(CommentApiRoutes.BY_ID)
    @ApiOperation(value = "find Comment by id",notes = "use this if you need full information by Comment")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 404,message = "Comment not found"),
    })
    public OkResponse<CommentResponse> byId( @ApiParam(value = "Comment id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
    return  OkResponse.of(CommentMapping.getInstance().getResponse().convert(
            commentApiService.findByID(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
    ));
    }
    @GetMapping(CommentApiRoutes.ROOT)
    @ApiOperation(value = "search Comment",notes = "use this if you need find Comment by message/article/user id")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<SearchResponse<CommentResponse>> search(
            @ModelAttribute CommentSearchRequest request
            ){
        return  OkResponse.of(CommentMapping.getInstance().getSearch().convert(
                commentApiService.search(request)
        ));
    }

    @PutMapping(CommentApiRoutes.BY_ID)
    @ApiOperation(value = "update Comment",notes = "use this if you need update Comment")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400,message = "Comment ID invalid"),
    })
    public OkResponse<CommentResponse> updateById(
            @ApiParam(value = "Comment id") @PathVariable String id,
            @RequestBody CommentRequest commentRequest
            ) throws CommentNotExistException, NotAccessException, AuthException {
        return OkResponse.of(CommentMapping.getInstance().getResponse().convert(
                commentApiService.update(commentRequest)
        ));
    }

    @DeleteMapping(CommentApiRoutes.BY_ID)
    @ApiOperation(value = "delete Comment",notes = "use this if you need delete Comment")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<String> deleteById(@ApiParam(value = "Comment id") @PathVariable ObjectId id) throws NotAccessException, AuthException, ChangeSetPersister.NotFoundException {
        commentApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
