package com.foodtech.blog.photo.controller;

import com.foodtech.blog.album.exeception.AlbumNotExistException;
import com.foodtech.blog.base.api.request.SearchRequest;
import com.foodtech.blog.base.api.response.OkResponse;
import com.foodtech.blog.base.api.response.SearchResponse;
import com.foodtech.blog.photo.api.request.PhotoRequest;
import com.foodtech.blog.photo.api.request.PhotoSearchRequest;
import com.foodtech.blog.photo.api.response.PhotoResponse;
import com.foodtech.blog.photo.exeception.PhotoExistException;
import com.foodtech.blog.photo.exeception.PhotoNotExistException;
import com.foodtech.blog.photo.mapping.PhotoMapping;
import com.foodtech.blog.photo.routes.PhotoApiRoutes;
import com.foodtech.blog.photo.service.PhotoApiService;
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
@Api(value = "Photo api")
public class PhotoApiController {
    private final PhotoApiService photoApiService;


    @GetMapping(PhotoApiRoutes.BY_ID)
    @ApiOperation(value = "find Photo by id",notes = "use this if you need full information by Photo")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 404,message = "Photo not found"),
    })
    public OkResponse<PhotoResponse> byId( @ApiParam(value = "Photo id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
    return  OkResponse.of(PhotoMapping.getInstance().getResponse().convert(
            photoApiService.findByID(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
    ));
    }

    @GetMapping(PhotoApiRoutes.ROOT)
    @ApiOperation(value = "search Photo",notes = "use this if you need find Photo by ????")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<SearchResponse<PhotoResponse>> search(
            @ModelAttribute PhotoSearchRequest request
            ){
        return  OkResponse.of(PhotoMapping.getInstance().getSearch().convert(
                photoApiService.search(request)
        ));
    }

    @PutMapping(PhotoApiRoutes.BY_ID)
    @ApiOperation(value = "update Photo",notes = "use this if you need update Photo")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400,message = "Photo ID invalid"),
    })
    public OkResponse<PhotoResponse> updateById(
            @ApiParam(value = "Photo id") @PathVariable String id,
            @RequestBody PhotoRequest photoRequest
            ) throws PhotoNotExistException {
        return OkResponse.of(PhotoMapping.getInstance().getResponse().convert(
                photoApiService.update(photoRequest)
        ));
    }

    @DeleteMapping(PhotoApiRoutes.BY_ID)
    @ApiOperation(value = "delete Photo",notes = "use this if you need delete Photo")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<String> deleteById(@ApiParam(value = "Photo id") @PathVariable ObjectId id){
        photoApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
