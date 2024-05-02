package tech.task.clearsolutions.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class RoleRepositoryTests {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleRepositoryTests(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Test
    public void testPresentFindByName() {
        String roleName = "ADMIN";

        assertTrue(roleRepository.findByName(roleName).isPresent());
    }

    @Test
    public void testEmptyFindByName() {
        String roleName = "UNEXPECTED";

        assertTrue(roleRepository.findByName(roleName).isEmpty());
    }

}
