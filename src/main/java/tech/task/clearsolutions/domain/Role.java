package tech.task.clearsolutions.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "roles")
@EqualsAndHashCode(of = "name")
@ToString(of = "name")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NaturalId
    @Pattern(regexp = "^[A-Z]*$",
            message = "Role name must be written and in upper case format.")
    @Column(nullable = false, unique = true)
    @NotEmpty(message = "Role name must be written and in upper case format.")
    public String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    public List<User> users;

    @Override
    public String getAuthority() {
        return "ROLE_" + name;
    }

}
