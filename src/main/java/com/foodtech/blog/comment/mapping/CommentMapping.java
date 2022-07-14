package

        com.foodtech.blog.comment.mapping;


import com.foodtech.blog.base.api.response.SearchResponse;
import com.foodtech.blog.base.mapping.BaseMapping;
import com.foodtech.blog.comment.api.request.CommentRequest;
import com.foodtech.blog.comment.api.response.CommentResponse;
import com.foodtech.blog.comment.model.CommentDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentMapping {

    public static class RequestMapping  {

        public CommentDoc convert(CommentRequest commentRequest, ObjectId userId) {
            return CommentDoc.builder()
                    .id(commentRequest.getId())
                    .articleId(commentRequest.getArticleId())
                    .userId(userId)
                    .message(commentRequest.getMessage())
                    .build();
        }

    }

    public static class ResponseMapping extends BaseMapping<CommentDoc, CommentResponse> {
        @Override
        public CommentResponse convert(CommentDoc commentDoc) {
            return CommentResponse.builder()
                    .id(commentDoc.getId().toString())
                    .articleId(commentDoc.getArticleId().toString())
                    .userId(commentDoc.getUserId().toString())
                    .message(commentDoc.getMessage())
                    .build();
        }

        @Override
        public CommentDoc unmapping(CommentResponse commentResponse) {
            throw new RuntimeException("dont use this");
        }
    }


    public static class SearchMapping extends BaseMapping<SearchResponse<CommentDoc>, SearchResponse<CommentResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();

        @Override
        public SearchResponse<CommentResponse> convert(SearchResponse<CommentDoc> searchResponse) {
            return SearchResponse.of(
                    searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount()
            );

        }

        @Override
        public SearchResponse<CommentDoc> unmapping(SearchResponse<CommentResponse> commentResponses) {
            throw new RuntimeException("dont use this");
        }
    }

    private final RequestMapping request = new RequestMapping();
    private final ResponseMapping response = new ResponseMapping();
    private final SearchMapping search = new SearchMapping();

    public static CommentMapping getInstance() {
        return new CommentMapping();
    }
}

