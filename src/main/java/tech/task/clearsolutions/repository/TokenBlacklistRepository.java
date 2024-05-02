package tech.task.clearsolutions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.task.clearsolutions.domain.TokenBlacklist;

import java.util.Optional;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {

    Optional<TokenBlacklist> findByToken(String token);

}
