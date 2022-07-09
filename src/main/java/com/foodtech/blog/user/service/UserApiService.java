package com.foodtech.blog.user.service;

import com.foodtech.blog.user.api.request.RegistrationRequest;
import com.foodtech.blog.user.api.request.UserRequest;
import com.foodtech.blog.user.api.response.UserResponse;
import com.foodtech.blog.user.exeception.UserExistException;
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
public class UserApiService {
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    public UserDoc registration(RegistrationRequest request) throws UserExistException {
        if(userRepository.findByEmail(request.getEmail()).isPresent() == true){
            throw new UserExistException();
        }
        UserDoc userDoc = new UserDoc();
        userDoc.setEmail(request.getEmail());
        userDoc.setPassword(DigestUtils.md5DigestAsHex(request.getPassword().getBytes()));
        userDoc = userRepository.save(userDoc);
        return  userDoc;
    }

    public Optional<UserDoc> findByID(ObjectId id){
        return userRepository.findById(id);
    }
    public List<UserDoc> search(
             String queryString,
             Integer size,
             Long skip
    ){
        Criteria criteria = new Criteria();
        if(queryString != null && queryString!=""){
            criteria = criteria.orOperator(
                    Criteria.where("firstName").regex(queryString, "i"),
                    Criteria.where("lastName").regex(queryString, "i"),
                    Criteria.where("email").regex(queryString, "i")
            );
        }

        Query query = new Query(criteria);
        query.limit(size);
        query.skip(skip);

        return mongoTemplate.find(query, UserDoc.class);
    }

    public UserDoc update(UserRequest request) throws UserNotExistException {
        Optional<UserDoc> userDocOptional = userRepository.findById(request.getId());
        if(userDocOptional.isPresent() == false){
            throw new UserNotExistException();
        }
        UserDoc userDoc = userDocOptional.get();
        userDoc.setFirstName(request.getFirstName());
        userDoc.setLastName(request.getLastName());
        userDoc.setAddress(request.getAddress());
        userDoc.setCompany(request.getCompany());

        userRepository.save(userDoc);

        return userDoc;
    }

    public void delete(ObjectId id){
        userRepository.deleteById(id);
    }
}
