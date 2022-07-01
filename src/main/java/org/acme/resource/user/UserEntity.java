package org.acme.resource.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import lombok.*;
import org.acme.resource.role.RoleEntity;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String username;

    private String password;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = RoleEntity.class)
    @JsonIgnoreProperties("users")
    private RoleEntity role;

}
