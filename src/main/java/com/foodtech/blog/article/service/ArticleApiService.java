package com.foodtech.blog.article.service;

import com.foodtech.blog.article.mapping.ArticleMapping;
import com.foodtech.blog.base.api.request.SearchRequest;
import com.foodtech.blog.base.api.response.SearchResponse;
import com.foodtech.blog.article.api.request.ArticleRequest;

import com.foodtech.blog.article.exeception.ArticleExistException;
import com.foodtech.blog.article.exeception.ArticleNotExistException;
import com.foodtech.blog.article.model.ArticleDoc;
import com.foodtech.blog.article.repository.ArticleRepository;
import com.foodtech.blog.user.exeception.UserNotExistException;
import com.foodtech.blog.user.model.UserDoc;
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
public class ArticleApiService {
    private final ArticleRepository articleRepository;
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;

    public ArticleDoc create(ArticleRequest request) throws ArticleExistException, UserNotExistException {
        Optional<UserDoc> userDoc = userRepository.findById(request.getOwnerId());
        if(userDoc.isPresent() == false) throw new UserNotExistException();
        ArticleDoc articleDoc = ArticleMapping.getInstance().getRequest().convert(request);
        articleRepository.save(articleDoc);
        return  articleDoc;
    }

    public Optional<ArticleDoc> findByID(ObjectId id){
        return articleRepository.findById(id);
    }
    public SearchResponse<ArticleDoc> search(
             SearchRequest request
    ){
        Criteria criteria = new Criteria();
        if(request.getQuery() != null && request.getQuery()!=""){
            criteria = criteria.orOperator(
                    Criteria.where("title").regex(request.getQuery(), "i"),
                    Criteria.where("body").regex(request.getQuery(), "i")

            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, ArticleDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<ArticleDoc> articleDocs = mongoTemplate.find(query, ArticleDoc.class);
        return SearchResponse.of(articleDocs, count);
    }

    public ArticleDoc update(ArticleRequest request) throws ArticleNotExistException {
        Optional<ArticleDoc> articleDocOptional = articleRepository.findById(request.getId());
        if(articleDocOptional.isPresent() == false){
            throw new ArticleNotExistException();
        }
        ArticleDoc oldDoc = articleDocOptional.get();
        ArticleDoc articleDoc = ArticleMapping.getInstance().getRequest().convert(request);;
        articleDoc.setId(request.getId());
    articleDoc.setOwnerId(oldDoc.getOwnerId());
        articleRepository.save(articleDoc);

        return articleDoc;
    }

    public void delete(ObjectId id){
        articleRepository.deleteById(id);
    }
}
