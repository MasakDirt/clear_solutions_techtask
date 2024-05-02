package tech.task.clearsolutions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.task.clearsolutions.domain.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.birthDate BETWEEN :startDate AND :endDate")
    List<User> findByBirthDateRange(@Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate);

    Optional<User> findByEmail(String email);

}
