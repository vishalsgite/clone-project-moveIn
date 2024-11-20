package com.vishal.project.moveInSync.moveInSyncApp.advices;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

       List<String> allowedRoutes = List.of("/v3/api-docs", "/actuator");
       boolean isAllowed = allowedRoutes.stream().anyMatch(routes -> request.getURI()
               .getPath().contains(routes));

        if(body instanceof ApiResponse<?> || isAllowed){
            return body;
        }
        return new ApiResponse<>(body);
    }
}
