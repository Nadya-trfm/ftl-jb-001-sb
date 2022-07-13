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
import com.foodtech.blog.user.exeception.UserNotExistException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Api(value = "File api")
public class FileController {
    private final FileApiService fileApiService;

    @PostMapping(FileApiRoutes.ROOT)
    @ApiOperation(value = "Create",notes="use this when you need create new File")
    @ApiResponses(value = {
           @ApiResponse(code = 200,message = "Success"),
           @ApiResponse(code = 400,message = "File already exist")
    })
    public @ResponseBody OkResponse<FileResponse> create(@RequestParam MultipartFile file, @RequestParam ObjectId ownerId) throws FileExistException, IOException, UserNotExistException {
        return OkResponse.of(FileMapping.getInstance().getResponse().convert(fileApiService.create(file, ownerId)));
    }

    @GetMapping(FileApiRoutes.DOWNLOAD)
    @ApiOperation(value = "find File by id",notes = "use this if you need full information by File")
    public void byId( @ApiParam(value = "File id")
                          @PathVariable ObjectId id, HttpServletResponse response)
            throws ChangeSetPersister.NotFoundException, IOException {
        FileCopyUtils.copy(fileApiService.downloadById(id), response.getOutputStream());
    }

}
