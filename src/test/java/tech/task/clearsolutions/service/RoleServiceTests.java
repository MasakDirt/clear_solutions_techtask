package tech.task.clearsolutions.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import tech.task.clearsolutions.domain.Role;
import tech.task.clearsolutions.repository.RoleRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class RoleServiceTests {
    private final RoleService roleService;
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceTests(RoleService roleService, RoleRepository roleRepository) {
        this.roleService = roleService;
        this.roleRepository = roleRepository;
    }

    @Test
    public void getByNameSuccess_Spring() {
        String roleName = "NEWSPRINGROLE";
        Role expected = getRoleWithName(roleName);

        roleRepository.save(expected);
        Role actual = roleService.getByName(roleName);

        assertEquals(expected, actual);
    }

    @Test
    public void getByNameNotFound_Spring() {
        String roleName = "Unexpected";
        assertThrows(EntityNotFoundException.class, () -> roleService.getByName(roleName));
    }

    private Role getRoleWithName(String name) {
        Role role = new Role();
        role.setName(name);

        return role;
    }

}
