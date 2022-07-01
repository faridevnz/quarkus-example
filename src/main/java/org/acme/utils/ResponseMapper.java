package org.acme.utils;

import javax.ws.rs.core.Response;
import java.util.List;

import static org.jboss.resteasy.reactive.RestResponse.StatusCode.CREATED;
import static org.jboss.resteasy.reactive.RestResponse.StatusCode.OK;

public class ResponseMapper {

    public static <T> Response okResp(T entity) {
        return Response.ok(entity).status(OK).build();
    }

    public static <T> Response okResp(List<T> entities) {
        return Response.ok(entities).status(OK).build();
    }

    public static <T> Response createdResp(T entity) {
        return Response.ok(entity).status(CREATED).build();
    }

}
