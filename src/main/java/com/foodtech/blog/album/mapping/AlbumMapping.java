package

        com.foodtech.blog.album.mapping;


import com.foodtech.blog.base.api.response.SearchResponse;
import com.foodtech.blog.base.mapping.BaseMapping;
import com.foodtech.blog.album.api.request.AlbumRequest;
import com.foodtech.blog.album.api.response.AlbumResponse;
import com.foodtech.blog.album.model.AlbumDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AlbumMapping {

    public static class RequestMapping extends BaseMapping<AlbumRequest, AlbumDoc> {

        @Override
        public AlbumDoc convert(AlbumRequest albumRequest) {
            return AlbumDoc.builder()
                    .id(albumRequest.getId())
                    .title(albumRequest.getTitle())
                    .ownerId(albumRequest.getOwnerId())
                    .build();
        }

        @Override
        public AlbumRequest unmapping(AlbumDoc albumDoc) {
            throw new RuntimeException("dont use this");
        }
    }

    public static class ResponseMapping extends BaseMapping<AlbumDoc, AlbumResponse> {
        @Override
        public AlbumResponse convert(AlbumDoc albumDoc) {
            return AlbumResponse.builder()
                    .id(albumDoc.getId().toString())
                    .title(albumDoc.getTitle())
                    .ownerId(albumDoc.getOwnerId().toString())
                    .build();
        }

        @Override
        public AlbumDoc unmapping(AlbumResponse albumResponse) {
            throw new RuntimeException("dont use this");
        }
    }


    public static class SearchMapping extends BaseMapping<SearchResponse<AlbumDoc>, SearchResponse<AlbumResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();

        @Override
        public SearchResponse<AlbumResponse> convert(SearchResponse<AlbumDoc> searchResponse) {
            return SearchResponse.of(
                    searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount()
            );

        }

        @Override
        public SearchResponse<AlbumDoc> unmapping(SearchResponse<AlbumResponse> albumResponses) {
            throw new RuntimeException("dont use this");
        }
    }

    private final RequestMapping request = new RequestMapping();
    private final ResponseMapping response = new ResponseMapping();
    private final SearchMapping search = new SearchMapping();

    public static AlbumMapping getInstance() {
        return new AlbumMapping();
    }
}

