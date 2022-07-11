package com.foodtech.blog.user.mapping;

import com.foodtech.blog.base.api.response.SearchResponse;
import com.foodtech.blog.base.mapping.BaseMapping;
import com.foodtech.blog.user.api.request.UserRequest;
import com.foodtech.blog.user.api.response.UserFullResponse;
import com.foodtech.blog.user.api.response.UserResponse;
import com.foodtech.blog.user.model.UserDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserMapping {
    public static class RequestMapping extends BaseMapping<UserRequest,UserDoc> {

        @Override
        public UserDoc convert(UserRequest userRequest){
            return UserDoc.builder()
                    .id(userRequest.getId())
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .email(userRequest.getEmail())
                    .build();
        }

        @Override
        public UserRequest unmapping(UserDoc userDoc) {
            throw new RuntimeException("dont use this");
        }
    }
    public static class ResponseMapping extends BaseMapping<UserDoc,UserResponse> {
       @Override
        public UserResponse convert(UserDoc userDoc){
            return UserResponse.builder()
                    .id(userDoc.getId().toString())
                    .firstName(userDoc.getFirstName())
                    .lastName(userDoc.getLastName())
                    .email(userDoc.getEmail())
                    .build();
        }

        @Override
        public UserDoc unmapping(UserResponse userResponse) {
            throw new RuntimeException("dont use this");
        }
    }
    public static class ResponseFullMapping extends BaseMapping<UserDoc, UserFullResponse> {
       @Override
        public UserFullResponse convert(UserDoc userDoc){
            return UserFullResponse.builder()
                    .id(userDoc.getId().toString())
                    .firstName(userDoc.getFirstName())
                    .lastName(userDoc.getLastName())
                    .email(userDoc.getEmail())
                    .address(userDoc.getAddress())
                    .company(userDoc.getCompany())
                    .build();
        }

        @Override
        public UserDoc unmapping(UserFullResponse userFullResponse) {
            throw new RuntimeException("dont use this");
        }
    }

    public static class SearchMapping extends BaseMapping<SearchResponse<UserDoc>, SearchResponse<UserResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();

        @Override
        public SearchResponse<UserResponse> convert(SearchResponse<UserDoc> searchRasponse) {
            return SearchResponse.of(
                    searchRasponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchRasponse.getCount()
            );

        }

        @Override
        public SearchResponse<UserDoc> unmapping(SearchResponse<UserResponse> userResponses) {
            throw new RuntimeException("dont use this");
        }
    }

    private final RequestMapping request = new RequestMapping();
    private final ResponseMapping response = new ResponseMapping();
    private final ResponseFullMapping responseFull = new ResponseFullMapping();
    private final SearchMapping search = new SearchMapping();

    public static UserMapping getInstance(){
        return new UserMapping();
    }
}
