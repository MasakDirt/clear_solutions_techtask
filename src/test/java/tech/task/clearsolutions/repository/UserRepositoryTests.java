package tech.task.clearsolutions.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tech.task.clearsolutions.domain.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class UserRepositoryTests {
    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTests(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    public void testFindByBirthDateRangeSuccess() {
        LocalDate from = LocalDate.of(1990, 1, 1);
        LocalDate to = LocalDate.of(2020, 1, 1);
        List<User> expected = userRepository.findAll()
                .stream()
                .filter(user -> user.getBirthDate().isAfter(from) &&
                        user.getBirthDate().isBefore(to))
                .toList();

        List<User> actual = userRepository.findByBirthDateRange(from, to);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByBirthDateRangeEmpty() {
        assertEquals(0, userRepository.findByBirthDateRange(LocalDate.of(2020, 1, 1),
                LocalDate.of(2100, 1, 1)).size());
    }

}
