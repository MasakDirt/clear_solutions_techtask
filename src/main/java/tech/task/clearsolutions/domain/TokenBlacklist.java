package tech.task.clearsolutions.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "token_blacklist")
@NoArgsConstructor
public class TokenBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "Token can't be empty!")
    private String token;

    private TokenBlacklist(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenBlacklist that = (TokenBlacklist) o;
        return this.id != null && this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 17 * getClass().hashCode();
    }

    @Override
    public String toString() {
        return "TokenBlacklist{" +
                "id=" + id +
                '}';
    }

    public static TokenBlacklist of(String token) {
        return new TokenBlacklist(token);
    }

}
