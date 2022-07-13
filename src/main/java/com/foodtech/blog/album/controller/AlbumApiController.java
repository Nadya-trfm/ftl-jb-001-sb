package com.foodtech.blog.album.controller;

import com.foodtech.blog.base.api.request.SearchRequest;
import com.foodtech.blog.base.api.response.OkResponse;
import com.foodtech.blog.base.api.response.SearchResponse;
import com.foodtech.blog.album.api.request.AlbumRequest;
import com.foodtech.blog.album.api.response.AlbumResponse;
import com.foodtech.blog.album.exeception.AlbumExistException;
import com.foodtech.blog.album.exeception.AlbumNotExistException;
import com.foodtech.blog.album.mapping.AlbumMapping;
import com.foodtech.blog.album.routes.AlbumApiRoutes;
import com.foodtech.blog.album.service.AlbumApiService;
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
@Api(value = "Album api")
public class AlbumApiController {
    private final AlbumApiService albumApiService;

    @PostMapping(AlbumApiRoutes.ROOT)
    @ApiOperation(value = "Create",notes="use this when you need create new Album")
    @ApiResponses(value = {
           @ApiResponse(code = 200,message = "Success"),
           @ApiResponse(code = 400,message = "Album already exist")
    })
    public OkResponse<AlbumResponse> create(@RequestBody AlbumRequest request) throws AlbumExistException, UserNotExistException {
        return OkResponse.of(AlbumMapping.getInstance().getResponse().convert(albumApiService.create(request)));
    }

    @GetMapping(AlbumApiRoutes.BY_ID)
    @ApiOperation(value = "find Album by id",notes = "use this if you need full information by Album")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 404,message = "Album not found"),
    })
    public OkResponse<AlbumResponse> byId( @ApiParam(value = "Album id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
    return  OkResponse.of(AlbumMapping.getInstance().getResponse().convert(
            albumApiService.findByID(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
    ));
    }
    @GetMapping(AlbumApiRoutes.ROOT)
    @ApiOperation(value = "search Album",notes = "use this if you need find Album by ????")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<SearchResponse<AlbumResponse>> search(
            @ModelAttribute SearchRequest request
            ){
        return  OkResponse.of(AlbumMapping.getInstance().getSearch().convert(
                albumApiService.search(request)
        ));
    }

    @PutMapping(AlbumApiRoutes.BY_ID)
    @ApiOperation(value = "update Album",notes = "use this if you need update Album")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400,message = "Album ID invalid"),
    })
    public OkResponse<AlbumResponse> updateById(
            @ApiParam(value = "Album id") @PathVariable String id,
            @RequestBody AlbumRequest albumRequest
            ) throws AlbumNotExistException {
        return OkResponse.of(AlbumMapping.getInstance().getResponse().convert(
                albumApiService.update(albumRequest)
        ));
    }

    @DeleteMapping(AlbumApiRoutes.BY_ID)
    @ApiOperation(value = "delete Album",notes = "use this if you need delete Album")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<String> deleteById(@ApiParam(value = "Album id") @PathVariable ObjectId id){
        albumApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
