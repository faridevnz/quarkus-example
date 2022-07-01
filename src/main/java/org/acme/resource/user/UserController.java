package org.acme.resource.user;

import io.smallrye.mutiny.Uni;
import org.acme.utils.ResponseMapper;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/users")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    private final UserRepository userRepository;

    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GET
    @Path("")
    public Uni<Response> index() {
        return this.userRepository.findAll()
                .onItem().transform(ResponseMapper::okResp);
    }

    @GET
    @Path("{id}")
    public Uni<Response> get(@PathParam("id") Long id) {
        return this.userRepository.findById(id)
                .onItem().ifNotNull().transform(ResponseMapper::okResp);
    }

    @POST
    @Path("")
    public Uni<Response> create(UserEntity userEntity) throws BadRequestException {
        return this.userRepository.create(userEntity)
                .onItem().transform(ResponseMapper::createdResp);
    }

    @PUT
    @Path("{id}")
    public Uni<Response> update(@PathParam("id") Long id, UserEntity userEntity) {
        return this.userRepository.update(id, userEntity)
                .onItem().transform(ResponseMapper::okResp);
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(@PathParam("id") Long id) {
        return this.userRepository.delete(id)
                .onItem().transform(ResponseMapper::okResp);
    }

}
