package com.foodtech.blog.user.service;

import com.foodtech.blog.user.api.request.RegistrationRequest;
import com.foodtech.blog.user.exeception.UserExistException;
import com.foodtech.blog.user.model.UserDoc;
import com.foodtech.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

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
}
