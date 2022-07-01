package org.acme.resource.user;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class UserRepository {

    /**
     * FIND ALL
     *
     * @return Uni<List<UserEntity>> - the list of all Users
     */
    public Uni<List<UserEntity>> findAll() {
        return UserEntity.findAll().list();
    }

    /**
     * FIND BY ID
     *
     * @param id Long - the User id
     * @return Uni<UserEntity> - the founded User
     * @throws NotFoundException if User with the given id does not exist
     */
    public Uni<UserEntity> findById(Long id) throws NotFoundException {
        return UserEntity
                .findById(id)
                .onItem().ifNull().failWith(NotFoundException::new)
                .onItem().ifNotNull().transform(user -> (UserEntity) user);
    }

    /**
     * FIND BY EMAIL
     *
     * @param email String - email to filter the User
     * @return Uni<UserEntity> - the founded User
     * @throws NotFoundException if User with given email does not exists
     */
    public Uni<UserEntity> findByEmail(String email) throws NotFoundException {
        return UserEntity.find("email", email)
                .firstResult()
                .onItem().ifNull().failWith(NotFoundException::new)
                .onItem().ifNotNull().transform(user -> (UserEntity) user);
    }

    /**
     * CREATE
     *
     * @param user UserEntity - the User to store
     * @return Uni<UserEntity> - the stored User
     * @throws BadRequestException - if the creation does not work
     */
    public Uni<UserEntity> create(UserEntity user) throws BadRequestException {
        return Panache
                .withTransaction(user::persist)
                .replaceWith(user)
                .onItem().ifNull().failWith(BadRequestException::new);
    }

    /**
     * UPDATE
     *
     * @param id Long - the id of the User to update
     * @param entity UserEntity - the User data to store
     * @return Uni<UserEntity> - the updated User data
     * @throws NotFoundException - if the User with the given id does not exist
     */
    public Uni<UserEntity> update(Long id, UserEntity entity) throws NotFoundException {
        return Panache
                .withTransaction(
                        () -> this.findById(id)
                        .onItem().ifNull().failWith(NotFoundException::new)
                        .onItem().ifNotNull()
                        .transform(user -> userMapper(user, entity))
                );
    }

    /**
     * DELETE
     *
     * @param id Long - the id of the User to delete
     * @return Uni<UserEntity> - the deleted User
     * @throws NotFoundException - if the User with the given id does not exist
     */
    public Uni<UserEntity> delete(Long id) throws NotFoundException {
        return Panache.withTransaction(
                () -> this.findById(id)
                .onItem().ifNull().failWith(NotFoundException::new)
                .onItem().ifNotNull().call(
                        user -> UserEntity
                        .deleteById(id)
                        .replaceWith(user)
                )
        );
    }

    private UserEntity userMapper(UserEntity from, UserEntity to) {
        from.setEmail(to.getEmail());
        from.setUsername(to.getUsername());
        from.setPassword(to.getPassword());
        return from;
    }

}
