package com.foodtech.blog.user.controller;

import com.foodtech.blog.user.api.request.RegistrationRequest;
import com.foodtech.blog.user.api.response.UserFullResponse;
import com.foodtech.blog.user.api.response.UserResponse;
import com.foodtech.blog.user.exeception.UserExistException;
import com.foodtech.blog.user.mapping.UserMapping;
import com.foodtech.blog.user.routes.UserApiRoutes;
import com.foodtech.blog.user.service.UserApiService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserApiService userApiService;

    @PostMapping(UserApiRoutes.ROOT)
    public UserFullResponse registration(@RequestBody RegistrationRequest request) throws UserExistException {
        return UserMapping.getInstance().getResponseFull().convert(userApiService.registration(request));
    }

    @GetMapping(UserApiRoutes.BY_ID)
    public UserFullResponse byId(@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
    return  UserMapping.getInstance().getResponseFull().convert(
            userApiService.findByID(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
    );
    }
    @GetMapping(UserApiRoutes.ROOT)
    public List<UserResponse> search(){
        return  UserMapping.getInstance().getSearch().convert(
                userApiService.search()
        );
    }
}
