package com.foodtech.blog.comment.service;

import com.foodtech.blog.article.exeception.ArticleNotExistException;
import com.foodtech.blog.article.repository.ArticleRepository;
import com.foodtech.blog.base.api.request.SearchRequest;
import com.foodtech.blog.base.api.response.SearchResponse;
import com.foodtech.blog.comment.api.request.CommentRequest;
import com.foodtech.blog.comment.api.request.CommentSearchRequest;
import com.foodtech.blog.comment.mapping.CommentMapping;
import com.foodtech.blog.comment.api.response.CommentResponse;
import com.foodtech.blog.comment.exeception.CommentExistException;
import com.foodtech.blog.comment.exeception.CommentNotExistException;
import com.foodtech.blog.comment.model.CommentDoc;
import com.foodtech.blog.comment.repository.CommentRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentApiService {
    private final CommentRepository commentRepository;
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public CommentDoc create(CommentRequest request) throws  UserNotExistException, ArticleNotExistException {
        if(userRepository.findById(request.getUserId()).isPresent() == false) throw new UserNotExistException();
        if(articleRepository.findById(request.getArticleId()).isPresent() == false) throw new ArticleNotExistException();
        CommentDoc commentDoc = CommentMapping.getInstance().getRequest().convert(request);
        commentRepository.save(commentDoc);
        return commentDoc;
    }

    public Optional<CommentDoc> findByID(ObjectId id) {
        return commentRepository.findById(id);
    }

    public SearchResponse<CommentDoc> search(
            CommentSearchRequest request
    ) {
        List<Criteria> orCriterias = new ArrayList<>();

        if (request.getQuery() != null && request.getQuery() != "") {
            orCriterias.add(Criteria.where("message").regex(request.getQuery(), "i"));
        }

        if(request.getArticleId() != null){
            orCriterias.add(Criteria.where("articleId").is(request.getArticleId()));
        }

        if(request.getUserId() != null){
            orCriterias.add(Criteria.where("userId").is(request.getUserId()));
        }

        Criteria criteria = new Criteria();

        if(orCriterias.size() > 0){
            criteria = criteria.orOperator(
                    orCriterias.toArray(new Criteria[orCriterias.size()])
            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, CommentDoc.class);

        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<CommentDoc> commentDocs = mongoTemplate.find(query, CommentDoc.class);
        return SearchResponse.of(commentDocs, count);
    }

    public CommentDoc update(CommentRequest request) throws CommentNotExistException {
        Optional<CommentDoc> commentDocOptional = commentRepository.findById(request.getId());

        if (commentDocOptional.isPresent() == false) {
            throw new CommentNotExistException();
        }
        CommentDoc oldDoc = commentDocOptional.get();

        CommentDoc commentDoc = CommentMapping.getInstance().getRequest().convert(request);
        commentDoc.setId(oldDoc.getId());
        commentDoc.setArticleId(oldDoc.getArticleId());
        commentDoc.setUserId(oldDoc.getUserId());

        commentRepository.save(commentDoc);

        return commentDoc;
    }

    public void delete(ObjectId id) {
        commentRepository.deleteById(id);
    }
}
