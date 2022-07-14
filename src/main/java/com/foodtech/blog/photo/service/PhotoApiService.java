package com.foodtech.blog.photo.service;

import com.foodtech.blog.album.exeception.AlbumNotExistException;
import com.foodtech.blog.album.model.AlbumDoc;
import com.foodtech.blog.album.repository.AlbumRepository;
import com.foodtech.blog.auth.exceptions.AuthException;
import com.foodtech.blog.auth.exceptions.NotAccessException;
import com.foodtech.blog.auth.service.AuthService;
import com.foodtech.blog.base.api.response.SearchResponse;
import com.foodtech.blog.base.servise.CheckAccess;
import com.foodtech.blog.photo.api.request.PhotoRequest;
import com.foodtech.blog.photo.api.request.PhotoSearchRequest;
import com.foodtech.blog.photo.mapping.PhotoMapping;
import com.foodtech.blog.photo.exeception.PhotoNotExistException;
import com.foodtech.blog.photo.model.PhotoDoc;
import com.foodtech.blog.photo.repository.PhotoRepository;
import com.foodtech.blog.base.api.model.UserDoc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoApiService extends CheckAccess<PhotoDoc> {
    private final PhotoRepository photoRepository;
    private final MongoTemplate mongoTemplate;
    private final AlbumRepository albumRepository;
    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations operations;
    private final AuthService authService;

    public PhotoDoc create(MultipartFile file, ObjectId albumId) throws AlbumNotExistException, IOException, AuthException, NotAccessException {
        UserDoc userDoc = authService.currentUser();
        AlbumDoc albumDoc =albumRepository.findById(albumId).orElseThrow(AlbumNotExistException::new);

        if(albumDoc.getOwnerId().equals(userDoc.getId()) == false) throw new NotAccessException();

        DBObject metaData = new BasicDBObject();
        metaData.put("type",file.getContentType());
        metaData.put("title",file.getOriginalFilename());

        ObjectId id = gridFsTemplate.store(
                file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metaData
        );

        PhotoDoc photoDoc = PhotoDoc.builder()
                .id(id)
                .albumId(albumId)
                .title(file.getOriginalFilename())
                .ownerId(userDoc.getId())
                .contentType(file.getContentType())
                .build();
        photoRepository.save(photoDoc);
        return  photoDoc;
    }

    public InputStream downloadById(ObjectId id) throws ChangeSetPersister.NotFoundException, IOException {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        if(file == null) throw new ChangeSetPersister.NotFoundException();
        return operations.getResource(file).getInputStream();
    }

    public Optional<PhotoDoc> findByID(ObjectId id){
        return photoRepository.findById(id);
    }
    public SearchResponse<PhotoDoc> search(
             PhotoSearchRequest request
    ){
        Criteria criteria = Criteria.where("albumId").is(request.getAlbumId());
        if(request.getQuery() != null && request.getQuery()!=""){
            criteria = criteria.orOperator(
                    Criteria.where("title").regex(request.getQuery(), "i")

            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, PhotoDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<PhotoDoc> photoDocs = mongoTemplate.find(query, PhotoDoc.class);
        return SearchResponse.of(photoDocs, count);
    }

    public PhotoDoc update(PhotoRequest request) throws PhotoNotExistException, NotAccessException, AuthException {
        Optional<PhotoDoc> photoDocOptional = photoRepository.findById(request.getId());
        if(photoDocOptional.isPresent() == false){
            throw new PhotoNotExistException();
        }
        PhotoDoc oldDoc = photoDocOptional.get();
        UserDoc owner = checkAccess(oldDoc);
        PhotoDoc photoDoc = PhotoMapping.getInstance().getRequest().convert(request, owner.getId());

        photoDoc.setId(request.getId());
        photoDoc.setAlbumId(oldDoc.getAlbumId());
        photoDoc.setOwnerId(oldDoc.getOwnerId());
        photoDoc.setContentType(oldDoc.getContentType());
        photoRepository.save(photoDoc);

        return photoDoc;
    }

    public void delete(ObjectId id) throws NotAccessException, AuthException, ChangeSetPersister.NotFoundException {
        checkAccess(photoRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new));
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(id)));
        photoRepository.deleteById(id);
    }

    @Override
    protected ObjectId getOwnerFromEntity(PhotoDoc entity) {
        return entity.getOwnerId();
    }

    @Override
    protected AuthService authService() {
        return authService;
    }
}
