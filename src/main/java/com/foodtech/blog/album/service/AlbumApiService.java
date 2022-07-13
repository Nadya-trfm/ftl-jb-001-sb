package com.foodtech.blog.album.service;

import com.foodtech.blog.base.api.request.SearchRequest;
import com.foodtech.blog.base.api.response.SearchResponse;
import com.foodtech.blog.album.api.request.AlbumRequest;
import com.foodtech.blog.album.mapping.AlbumMapping;
import com.foodtech.blog.album.api.response.AlbumResponse;
import com.foodtech.blog.album.exeception.AlbumExistException;
import com.foodtech.blog.album.exeception.AlbumNotExistException;
import com.foodtech.blog.album.model.AlbumDoc;
import com.foodtech.blog.album.repository.AlbumRepository;
import com.foodtech.blog.photo.api.request.PhotoSearchRequest;
import com.foodtech.blog.photo.model.PhotoDoc;
import com.foodtech.blog.photo.repository.PhotoRepository;
import com.foodtech.blog.photo.service.PhotoApiService;
import com.foodtech.blog.user.exeception.UserNotExistException;
import com.foodtech.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlbumApiService {
    private final AlbumRepository albumRepository;
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;
    private final PhotoApiService photoApiService;

    public AlbumDoc create(AlbumRequest request) throws AlbumExistException, UserNotExistException {
        if(userRepository.findById(request.getOwnerId()).isEmpty()) throw new UserNotExistException();

        AlbumDoc albumDoc = AlbumMapping.getInstance().getRequest().convert(request);
        albumRepository.save(albumDoc);
        return albumDoc;
    }

    public Optional<AlbumDoc> findByID(ObjectId id) {
        return albumRepository.findById(id);
    }

    public SearchResponse<AlbumDoc> search(
            SearchRequest request
    ) {
        Criteria criteria = new Criteria();
        if (request.getQuery() != null && request.getQuery() != "") {
            criteria = criteria.orOperator(
                    Criteria.where("title").regex(request.getQuery(), "i")

                    );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, AlbumDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<AlbumDoc> albumDocs = mongoTemplate.find(query, AlbumDoc.class);
        return SearchResponse.of(albumDocs, count);
    }

    public AlbumDoc update(AlbumRequest request) throws AlbumNotExistException {
        Optional<AlbumDoc> albumDocOptional = albumRepository.findById(request.getId());
        if (albumDocOptional.isPresent() == false) {
            throw new AlbumNotExistException();
        }
        AlbumDoc oldDoc = albumDocOptional.get();
        AlbumDoc albumDoc = AlbumMapping.getInstance().getRequest().convert(request);
        ;
        albumDoc.setId(request.getId());
        albumDoc.setOwnerId(oldDoc.getOwnerId());
        albumRepository.save(albumDoc);

        return albumDoc;
    }

    public void delete(ObjectId id) {
        List<PhotoDoc> photoDocs = photoApiService
                .search(PhotoSearchRequest.builder().albumId(id).size(10000).build())
                .getList();

        for(PhotoDoc photoDoc: photoDocs){
            photoApiService.delete(photoDoc.getId());
        }

        albumRepository.deleteById(id);
    }
}
