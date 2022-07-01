package org.acme.resource.role;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class RoleRepository {

    public Uni<List<RoleEntity>> findAll() {
        return RoleEntity.findAll().list();
    }

    public Uni<RoleEntity> findById(Long id) {
        return RoleEntity.findById(id)
                .onItem().ifNull().failWith(NotFoundException::new)
                .onItem().ifNotNull().transform(role -> (RoleEntity) role);
    }

    public Uni<RoleEntity> create(RoleEntity roleEntity) {
        return Panache.withTransaction(
                () -> roleEntity.persist()
                        .onFailure().transform(err -> new BadRequestException())
                        .<RoleEntity>onItem().transform(role -> (RoleEntity) role)

        );
    }

}
