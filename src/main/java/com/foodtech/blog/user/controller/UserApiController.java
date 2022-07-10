package com.foodtech.blog.user.controller;

import com.foodtech.blog.base.api.request.SearchRequest;
import com.foodtech.blog.base.api.response.OkResponse;
import com.foodtech.blog.base.api.response.SearchResponse;
import com.foodtech.blog.user.api.request.RegistrationRequest;
import com.foodtech.blog.user.api.request.UserRequest;
import com.foodtech.blog.user.api.response.UserFullResponse;
import com.foodtech.blog.user.api.response.UserResponse;
import com.foodtech.blog.user.exeception.UserExistException;
import com.foodtech.blog.user.exeception.UserNotExistException;
import com.foodtech.blog.user.mapping.UserMapping;
import com.foodtech.blog.user.routes.UserApiRoutes;
import com.foodtech.blog.user.service.UserApiService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserApiService userApiService;

    @PostMapping(UserApiRoutes.ROOT)
    public OkResponse<UserFullResponse> registration(@RequestBody RegistrationRequest request) throws UserExistException {
        return OkResponse.of(UserMapping.getInstance().getResponseFull().convert(userApiService.registration(request)));
    }

    @GetMapping(UserApiRoutes.BY_ID)
    public OkResponse<UserFullResponse> byId(@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
    return  OkResponse.of(UserMapping.getInstance().getResponseFull().convert(
            userApiService.findByID(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
    ));
    }
    @GetMapping(UserApiRoutes.ROOT)
    public OkResponse<SearchResponse<UserResponse>> search(
            @ModelAttribute SearchRequest request
            ){
        return  OkResponse.of(UserMapping.getInstance().getSearch().convert(
                userApiService.search(request)
        ));
    }

    @PutMapping(UserApiRoutes.BY_ID)
    public OkResponse<UserFullResponse> updateById(
            @PathVariable String id,
            @RequestBody UserRequest userRequest
            ) throws UserNotExistException {
        return OkResponse.of(UserMapping.getInstance().getResponseFull().convert(
                userApiService.update(userRequest)
        ));
    }

    @DeleteMapping(UserApiRoutes.BY_ID)
    public OkResponse<String> deleteById(@PathVariable ObjectId id){
        userApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
