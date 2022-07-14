package com.foodtech.blog.todoTask.controller;

import com.foodtech.blog.auth.exceptions.AuthException;
import com.foodtech.blog.auth.exceptions.NotAccessException;
import com.foodtech.blog.base.api.request.SearchRequest;
import com.foodtech.blog.base.api.response.OkResponse;
import com.foodtech.blog.base.api.response.SearchResponse;
import com.foodtech.blog.todoTask.api.request.TodoTaskRequest;
import com.foodtech.blog.todoTask.api.request.TodoTaskSearchRequest;
import com.foodtech.blog.todoTask.api.response.TodoTaskResponse;
import com.foodtech.blog.todoTask.exeception.TodoTaskExistException;
import com.foodtech.blog.todoTask.exeception.TodoTaskNotExistException;
import com.foodtech.blog.todoTask.mapping.TodoTaskMapping;
import com.foodtech.blog.todoTask.routes.TodoTaskApiRoutes;
import com.foodtech.blog.todoTask.service.TodoTaskApiService;
import com.foodtech.blog.user.exeception.UserNotExistException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "TodoTask api")
public class TodoTaskApiController {
    private final TodoTaskApiService todoTaskApiService;

    @PostMapping(TodoTaskApiRoutes.ROOT)
    @ApiOperation(value = "Create",notes="use this when you need create new TodoTask")
    @ApiResponses(value = {
           @ApiResponse(code = 200,message = "Success"),
           @ApiResponse(code = 400,message = "TodoTask already exist")
    })
    public OkResponse<TodoTaskResponse> create(@RequestBody TodoTaskRequest request) throws  AuthException {
        return OkResponse.of(TodoTaskMapping.getInstance().getResponse().convert(todoTaskApiService.create(request)));
    }

    @GetMapping(TodoTaskApiRoutes.BY_ID)
    @ApiOperation(value = "find TodoTask by id",notes = "use this if you need full information by TodoTask")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 404,message = "TodoTask not found"),
    })
    public OkResponse<TodoTaskResponse> byId( @ApiParam(value = "TodoTask id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
    return  OkResponse.of(TodoTaskMapping.getInstance().getResponse().convert(
            todoTaskApiService.findByID(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
    ));
    }
    @GetMapping(TodoTaskApiRoutes.ROOT)
    @ApiOperation(value = "search TodoTask",notes = "use this if you need find TodoTask by ????")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<SearchResponse<TodoTaskResponse>> search(
            @ModelAttribute TodoTaskSearchRequest request
            ) throws ResponseStatusException, AuthException {
        return  OkResponse.of(TodoTaskMapping.getInstance().getSearch().convert(
                todoTaskApiService.search(request)
        ));
    }

    @PutMapping(TodoTaskApiRoutes.BY_ID)
    @ApiOperation(value = "update TodoTask",notes = "use this if you need update TodoTask")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400,message = "TodoTask ID invalid"),
    })
    public OkResponse<TodoTaskResponse> updateById(
            @ApiParam(value = "TodoTask id") @PathVariable String id,
            @RequestBody TodoTaskRequest todoTaskRequest
            ) throws TodoTaskNotExistException, NotAccessException, AuthException {
        return OkResponse.of(TodoTaskMapping.getInstance().getResponse().convert(
                todoTaskApiService.update(todoTaskRequest)
        ));
    }

    @DeleteMapping(TodoTaskApiRoutes.BY_ID)
    @ApiOperation(value = "delete TodoTask",notes = "use this if you need delete TodoTask")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<String> deleteById(@ApiParam(value = "TodoTask id") @PathVariable ObjectId id) throws NotAccessException, AuthException {
        todoTaskApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
