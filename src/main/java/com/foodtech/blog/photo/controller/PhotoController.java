package com.foodtech.blog.photo.controller;

import com.foodtech.blog.album.exeception.AlbumNotExistException;
import com.foodtech.blog.base.api.response.OkResponse;
import com.foodtech.blog.photo.api.response.PhotoResponse;
import com.foodtech.blog.photo.mapping.PhotoMapping;
import com.foodtech.blog.photo.model.PhotoDoc;
import com.foodtech.blog.photo.routes.PhotoApiRoutes;
import com.foodtech.blog.photo.service.PhotoApiService;
import com.foodtech.blog.user.exeception.UserNotExistException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Api(value = "Photo api")
public class PhotoController {
    private final PhotoApiService photoApiService;

    @PostMapping(PhotoApiRoutes.ROOT)
    @ApiOperation(value = "Create",notes="use this when you need create new photo")
    @ApiResponses(value = {
           @ApiResponse(code = 200,message = "Success"),
           @ApiResponse(code = 400,message = "Photo already exist")
    })
    public @ResponseBody OkResponse<PhotoResponse> create(
            @RequestParam MultipartFile file,
            @RequestParam ObjectId ownerId,
            @RequestParam ObjectId albumId)
            throws IOException, UserNotExistException, AlbumNotExistException {
        return OkResponse.of(PhotoMapping.getInstance().getResponse().convert(photoApiService.create(file, ownerId, albumId)));
    }

    @GetMapping(PhotoApiRoutes.DOWNLOAD)
    @ApiOperation(value = "find photo by id",notes = "use this if you need full information by photo")
    public void byId( @ApiParam(value = "photo id")
                          @PathVariable ObjectId id, HttpServletResponse response)
            throws ChangeSetPersister.NotFoundException, IOException {
        PhotoDoc photoDoc = photoApiService.findByID(id).get();
        response.addHeader("Content-Type","image/png");
        response.addHeader("Content-Disposition", ": inline; filename=\""+photoDoc.getTitle()+"\"");
        FileCopyUtils.copy(photoApiService.downloadById(id), response.getOutputStream());
    }

}
