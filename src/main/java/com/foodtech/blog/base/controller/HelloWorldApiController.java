package com.foodtech.blog.base.controller;

import com.foodtech.blog.base.api.request.PostRequest;
import com.foodtech.blog.base.api.response.IndexResponse;
import com.foodtech.blog.base.routers.ExampleRoutes;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloWorldApiController {

    @GetMapping(value ="/")
    public IndexResponse index(){
        return IndexResponse.builder().message("Hello, World").build();
    }

    @GetMapping(ExampleRoutes.GET)
    public  IndexResponse getExample(){
        return IndexResponse.builder().message("Example routes. Get response").build();
    }

    @GetMapping(ExampleRoutes.GET_WITH_PARAM)
    public IndexResponse getExampleWithParam(
            @RequestParam String param1,
            @RequestParam(value ="p_a_r_a_m_2") String param2,
            @RequestParam(required = false) String param3,
            @RequestParam(required = false, defaultValue = "def") String param4
    ){
        return  IndexResponse.builder()
                .message(
                "ExampleRoutes.GET_WITH_PARAM: " +
                        "param1: "+ param1+"; " +
                        "param2: "+ param2+"; "+
                        "param3: "+ param3+"; "+
                        "param4: "+ param4+"; ").build();
    }

    @GetMapping(ExampleRoutes.GET_WITH_PATH)
    public IndexResponse getExampleWithPath( @PathVariable String pathVariable){
        return IndexResponse.builder()
                .message(pathVariable)
                .build();

    }

    @PostMapping(ExampleRoutes.POST)
    public IndexResponse post(
            @RequestBody PostRequest request
            ){
        return IndexResponse.builder()
                .message(request.toString())
                .build();
    }

    @PutMapping(ExampleRoutes.PUT)
    public  IndexResponse put(
            @PathVariable String id, @RequestBody PostRequest request
    ){
        return IndexResponse.builder()
                .message(id+" "+ request.toString())
                .build();
    }

    @DeleteMapping(ExampleRoutes.DELETE)
    public IndexResponse delete(@PathVariable String id){
        return  IndexResponse.builder()
                .message(id)
                .build();
    }
}
