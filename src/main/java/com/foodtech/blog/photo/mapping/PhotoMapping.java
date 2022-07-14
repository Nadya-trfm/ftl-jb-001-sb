package

        com.foodtech.blog.photo.mapping;


import com.foodtech.blog.base.api.response.SearchResponse;
import com.foodtech.blog.base.mapping.BaseMapping;
import com.foodtech.blog.photo.api.request.PhotoRequest;
import com.foodtech.blog.photo.api.response.PhotoResponse;
import com.foodtech.blog.photo.model.PhotoDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PhotoMapping {

    public static class RequestMapping {

        public PhotoDoc convert(PhotoRequest photoRequest, ObjectId ownerId) {
            return PhotoDoc.builder()
                    .id(photoRequest.getId())
                    .title(photoRequest.getTitle())
                    .ownerId(ownerId)
                    .albumId(photoRequest.getAlbumId())
                    .contentType(photoRequest.getContentType())
                    .build();
        }

    }

    public static class ResponseMapping extends BaseMapping<PhotoDoc, PhotoResponse> {
        @Override
        public PhotoResponse convert(PhotoDoc photoDoc) {
            return PhotoResponse.builder()
                    .id(photoDoc.getId().toString())
                    .title(photoDoc.getTitle())
                    .ownerId(photoDoc.getOwnerId().toString())
                    .albumId(photoDoc.getAlbumId().toString())
                    .contentType(photoDoc.getContentType())
                    .build();
        }

        @Override
        public PhotoDoc unmapping(PhotoResponse photoResponse) {
            throw new RuntimeException("dont use this");
        }
    }


    public static class SearchMapping extends BaseMapping<SearchResponse<PhotoDoc>, SearchResponse<PhotoResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();

        @Override
        public SearchResponse<PhotoResponse> convert(SearchResponse<PhotoDoc> searchResponse) {
            return SearchResponse.of(
                    searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount()
            );

        }

        @Override
        public SearchResponse<PhotoDoc> unmapping(SearchResponse<PhotoResponse> photoResponses) {
            throw new RuntimeException("dont use this");
        }
    }

    private final RequestMapping request = new RequestMapping();
    private final ResponseMapping response = new ResponseMapping();
    private final SearchMapping search = new SearchMapping();

    public static PhotoMapping getInstance() {
        return new PhotoMapping();
    }
}

