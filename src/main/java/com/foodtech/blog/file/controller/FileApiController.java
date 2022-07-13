package com.foodtech.blog.file.controller;

import com.foodtech.blog.base.api.request.SearchRequest;
import com.foodtech.blog.base.api.response.OkResponse;
import com.foodtech.blog.base.api.response.SearchResponse;
import com.foodtech.blog.file.api.response.FileResponse;
import com.foodtech.blog.file.exeception.FileExistException;
import com.foodtech.blog.file.exeception.FileNotExistException;
import com.foodtech.blog.file.mapping.FileMapping;
import com.foodtech.blog.file.routes.FileApiRoutes;
import com.foodtech.blog.file.service.FileApiService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "File api")
public class FileApiController {
    private final FileApiService fileApiService;


    @GetMapping(FileApiRoutes.BY_ID)
    @ApiOperation(value = "find File by id",notes = "use this if you need full information by File")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 404,message = "File not found"),
    })
    public OkResponse<FileResponse> byId( @ApiParam(value = "File id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
    return  OkResponse.of(FileMapping.getInstance().getResponse().convert(
            fileApiService.findByID(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
    ));
    }
    @GetMapping(FileApiRoutes.ROOT)
    @ApiOperation(value = "search File",notes = "use this if you need find File by ????")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<SearchResponse<FileResponse>> search(
            @ModelAttribute SearchRequest request
            ){
        return  OkResponse.of(FileMapping.getInstance().getSearch().convert(
                fileApiService.search(request)
        ));
    }

    @DeleteMapping(FileApiRoutes.BY_ID)
    @ApiOperation(value = "delete File",notes = "use this if you need delete File")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<String> deleteById(@ApiParam(value = "File id") @PathVariable ObjectId id){
        fileApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
