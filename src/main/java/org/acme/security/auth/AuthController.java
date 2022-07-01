package org.acme.security.auth;

import io.quarkus.security.UnauthorizedException;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import org.acme.resource.user.UserEntity;
import org.acme.resource.user.UserRepository;
import org.acme.security.Jwt;
import org.acme.security.auth.dto.LoginDto;
import org.acme.utils.ResponseMapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    private final UserRepository userRepository;

    AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @POST
    @Path("/login")
    public Uni<Response> login(LoginDto loginDto) {
        return this.userRepository
                .findByEmail(loginDto.getEmail())
                .onFailure().recoverWithUni(error -> Uni.createFrom().failure(new UnauthorizedException()))
                .onItem().transformToUni(
                        user -> verifyPassword(user, loginDto.getPassword())
                        .onItem().transform(Unchecked.function(valid -> {
                            if (!valid) throw new UnauthorizedException();
                            return Jwt.generateToken(user.getId());
                        }))
                        .onItem().transform(ResponseMapper::okResp)
                );
    }

    public Uni<Boolean> verifyPassword(UserEntity user, String password) {
        return Uni.createFrom().item(user.getPassword().equals(password));
    }

}
