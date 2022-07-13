package com.foodtech.blog.todoTask.service;

import com.foodtech.blog.base.api.request.SearchRequest;
import com.foodtech.blog.base.api.response.SearchResponse;
import com.foodtech.blog.todoTask.api.request.TodoTaskRequest;
import com.foodtech.blog.todoTask.api.request.TodoTaskSearchRequest;
import com.foodtech.blog.todoTask.mapping.TodoTaskMapping;
import com.foodtech.blog.todoTask.api.response.TodoTaskResponse;
import com.foodtech.blog.todoTask.exeception.TodoTaskExistException;
import com.foodtech.blog.todoTask.exeception.TodoTaskNotExistException;
import com.foodtech.blog.todoTask.model.TodoTaskDoc;
import com.foodtech.blog.todoTask.repository.TodoTaskRepository;
import com.foodtech.blog.user.exeception.UserNotExistException;
import com.foodtech.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoTaskApiService {
    private final TodoTaskRepository todoTaskRepository;
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;

    public TodoTaskDoc create(TodoTaskRequest request) throws TodoTaskExistException, UserNotExistException {
        if(userRepository.findById(request.getOwnerId()).isEmpty()) throw new UserNotExistException();

        TodoTaskDoc todoTaskDoc =TodoTaskMapping.getInstance().getRequest().convert(request);
        todoTaskRepository.save(todoTaskDoc);
        return  todoTaskDoc;
    }

    public Optional<TodoTaskDoc> findByID(ObjectId id){
        return todoTaskRepository.findById(id);
    }
    public SearchResponse<TodoTaskDoc> search(
             TodoTaskSearchRequest request
    ){
        if(request.getOwnerId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Criteria criteria = Criteria.where("ownerId").is(request.getOwnerId());
        if(request.getQuery() != null && request.getQuery()!=""){
            criteria = criteria.orOperator(
                    Criteria.where("title").regex(request.getQuery(), "i")

            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, TodoTaskDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<TodoTaskDoc> todoTaskDocs = mongoTemplate.find(query, TodoTaskDoc.class);
        return SearchResponse.of(todoTaskDocs, count);
    }

    public TodoTaskDoc update(TodoTaskRequest request) throws TodoTaskNotExistException {
        Optional<TodoTaskDoc> todoTaskDocOptional = todoTaskRepository.findById(request.getId());
        if(todoTaskDocOptional.isPresent() == false){
            throw new TodoTaskNotExistException();
        }
        TodoTaskDoc oldDoc = todoTaskDocOptional.get();

        TodoTaskDoc todoTaskDoc = TodoTaskMapping.getInstance().getRequest().convert(request);

        todoTaskDoc.setId(request.getId());
        todoTaskDoc.setOwnerId(oldDoc.getOwnerId());
        todoTaskRepository.save(todoTaskDoc);

        return todoTaskDoc;
    }

    public void delete(ObjectId id){
        todoTaskRepository.deleteById(id);
    }
}
