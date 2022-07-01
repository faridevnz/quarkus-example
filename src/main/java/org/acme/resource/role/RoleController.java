package org.acme.resource.role;

import io.smallrye.mutiny.Uni;
import org.acme.utils.ResponseMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoleController {

    private final RoleRepository roleRepository;

    RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GET
    @Path("")
    public Uni<Response> index() {
        return this.roleRepository.findAll()
                .onItem().transform(ResponseMapper::okResp);
    }

    @GET
    @Path("{id}")
    public Uni<Response> get(@PathParam("id") Long id) {
        return this.roleRepository.findById(id)
                .onItem().ifNotNull().transform(ResponseMapper::okResp);
    }

    @POST
    @Path("")
    public Uni<Response> create(RoleEntity roleEntity) throws BadRequestException {
        return this.roleRepository.create(roleEntity)
                .onItem().transform(ResponseMapper::createdResp);
    }

}
