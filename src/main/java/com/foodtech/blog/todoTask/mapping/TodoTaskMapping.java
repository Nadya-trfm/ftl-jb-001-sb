package

        com.foodtech.blog.todoTask.mapping;


import com.foodtech.blog.base.api.response.SearchResponse;
import com.foodtech.blog.base.mapping.BaseMapping;
import com.foodtech.blog.todoTask.api.request.TodoTaskRequest;
import com.foodtech.blog.todoTask.api.response.TodoTaskResponse;
import com.foodtech.blog.todoTask.model.TodoTaskDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TodoTaskMapping{

    public static class RequestMapping extends BaseMapping<TodoTaskRequest,TodoTaskDoc>{

        @Override
        public TodoTaskDoc convert(TodoTaskRequest todoTaskRequest){
            return TodoTaskDoc.builder()
                .id(todoTaskRequest.getId())
                .title(todoTaskRequest.getTitle())
                .ownerId(todoTaskRequest.getOwnerId())
                .completed(todoTaskRequest.getCompleted())
                .files(todoTaskRequest.getFiles())
            .build();
            }

        @Override
        public TodoTaskRequest unmapping(TodoTaskDoc todoTaskDoc){
            throw new RuntimeException("dont use this");
            }
    }

    public static class ResponseMapping extends BaseMapping<TodoTaskDoc,TodoTaskResponse>{
        @Override
        public TodoTaskResponse convert(TodoTaskDoc todoTaskDoc){
            return TodoTaskResponse.builder()
                .id(todoTaskDoc.getId().toString())
                .title(todoTaskDoc.getTitle())
                .ownerId(todoTaskDoc.getOwnerId().toString())
                .completed(todoTaskDoc.getCompleted())
                .files(todoTaskDoc.getFiles().stream().map(ObjectId::toString).collect(Collectors.toList()))
                .build();
            }

        @Override
        public TodoTaskDoc unmapping(TodoTaskResponse todoTaskResponse){
            throw new RuntimeException("dont use this");
            }
            }



    public static class SearchMapping extends BaseMapping<SearchResponse< TodoTaskDoc>,SearchResponse<TodoTaskResponse>>{
        private ResponseMapping responseMapping=new ResponseMapping();

        @Override
        public SearchResponse<TodoTaskResponse>convert(SearchResponse<TodoTaskDoc> searchResponse){
                return SearchResponse.of(
                searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                searchResponse.getCount()
                );

                }

        @Override
        public SearchResponse<TodoTaskDoc>unmapping(SearchResponse<TodoTaskResponse> todoTaskResponses){
                throw new RuntimeException("dont use this");
                }
                }

        private final RequestMapping request=new RequestMapping();
        private final ResponseMapping response=new ResponseMapping();
        private final SearchMapping search=new SearchMapping();

        public static TodoTaskMapping getInstance(){
            return new TodoTaskMapping();
            }
}

