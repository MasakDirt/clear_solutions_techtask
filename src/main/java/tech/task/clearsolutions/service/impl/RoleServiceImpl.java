package tech.task.clearsolutions.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.task.clearsolutions.domain.Role;
import tech.task.clearsolutions.repository.RoleRepository;
import tech.task.clearsolutions.service.RoleService;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getByName(String name) {
        return roleRepository.findByName(name).orElseThrow(
                () -> new EntityNotFoundException("Role not found"));
    }

}
